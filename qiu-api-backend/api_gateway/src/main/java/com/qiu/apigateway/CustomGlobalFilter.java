package com.qiu.apigateway;


import com.qiu.common.model.InvokeInterface;
import com.qiu.common.model.InvokeUser;
import com.qiu.common.service.InnerInterfaceService;
import com.qiu.common.service.InnerUserInterfaceService;
import com.qiu.common.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.qiu.apiclientsdk.utils.SignUtils.getSign;

/**
 * @description: 自定义全局过滤器
 * @className: CustomGlobalFilter.java
 * @author: qiu
 * @createTime: 2024/3/19 17:20
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceService innerInterfaceService;

    @DubboReference
    private InnerUserInterfaceService userInterfaceService;

    private final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1", "0:0:0:0:0:0:0:1");

    private static final String INTERFACE_HOST = "http://localhost:8083";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：{}", request.getId());
        log.info("请求路径：{}", request.getPath());
        log.info("请求方法：{}", request.getMethod());
        log.info("请求query参数：{}", request.getQueryParams());
        log.info("请求体：{}", request.getBody());
        String sourceAddress = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        log.info("请求来源地址：{}", sourceAddress);
        log.info("请求来源地址：{}", request.getRemoteAddress());
        ServerHttpResponse response = exchange.getResponse();
        // 访问控制 - 黑白名单
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            return handleNoAuth(response);
        }
        // 用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timeStamp = headers.getFirst("timeStamp");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String hostAddr = headers.getFirst("hostAddr");
        System.out.println("请求头：");
        System.out.println("accessKey:" + accessKey);
        System.out.println("nonce:" + nonce);
        System.out.println("timeStamp:" + timeStamp);
        System.out.println("hostAddr:" + hostAddr);
        System.out.println("body:" + body);
        System.out.println("sign:" + sign);
        // 从数据库取权限是否分配给用户
        if (sign == null) {
            return handleNoAuth(response);
        }
        InvokeUser invokeUser;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
            return handleNoAuth(response);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        String secretKey = invokeUser.getSecretKey();
        // 核对secretKey
        String checkSign = getSign(body, secretKey);
        if (!sign.equals(checkSign)) {
            return handleNoAuth(response);
        }
        // 时间和当前时间不能超过5分钟
        if (timeStamp == null) {
            return handleNoAuth(response);
        }
        long FIVE_MINUTES = 60 * 5L;
        if (Integer.parseInt(timeStamp) > Integer.parseInt(timeStamp) + FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        // 请求的模拟接口是否存在
        String path = request.getPath().value();
        String method = Objects.requireNonNull(request.getMethod()).toString();
        InvokeInterface invokeInterface;
        try {
            invokeInterface = innerInterfaceService.getInvokeInterface(hostAddr, path, method);
        } catch (Exception e) {
            log.error("getInvokeInterface error", e);
            return handleNoAuth(response);
        }
        if (invokeInterface == null) {
            return handleNoAuth(response);
        }
        if (response.getStatusCode() != HttpStatus.OK) {
            // 调用失败
            return handleInvokeError(response);
        }
        // 请求转发，调用模拟接口
        // 响应日志
        log.info("响应码：{}", response.getStatusCode());
        return handleResponse(exchange, chain, invokeInterface.getId(), invokeUser.getId());
    }

    /**
     * @description: 处理响应
     * @params: [exchange, chain]
     * @return: reactor.core.publisher.Mono<java.lang.Void> 
     * @author: qiu
     * @dateTime: 2024/3/19 19:53
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long interfaceId, Long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等待调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(fluxBody.handle((dataBuffers, sink) -> {
                                // 7.调用成功，接口调用次数+1 invokeCount
                                try {
                                    userInterfaceService.invokeCount(interfaceId, userId);
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                    sink.error(new RuntimeException("调用接口服务失败"));
                                    return;
                                }
                                byte[] content = new byte[dataBuffers.readableByteCount()];
                                dataBuffers.read(content);
                                DataBufferUtils.release(dataBuffers);//释放掉内存
                                // 构建返回日志
                                String data = new String(content, StandardCharsets.UTF_8); //data
                                // 打印日志
                                log.info("响应结果：{}", data);
                                sink.next(bufferFactory.wrap(content));
                            }));
                        } else {
                            // 调用失败，返回一个规范的错误码
                            log.error("<-- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常{}", String.valueOf(e));
            return chain.filter(exchange);
        }
    }

    private Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
