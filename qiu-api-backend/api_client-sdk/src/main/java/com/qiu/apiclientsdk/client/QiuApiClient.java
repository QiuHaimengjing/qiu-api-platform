package com.qiu.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qiu.apiclientsdk.model.InvokeParams;
import com.qiu.apiclientsdk.model.ResponseStatus;
import com.qiu.apiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 客户端
 * @className: QiuApiClient.java
 * @author: qiu
 * @createTime: 2024/3/14 16:18
 */
@Slf4j
public class QiuApiClient {

    private final String accessKey;

    private final String secretKey;

    private static final String GATEWAY_HOST = "http://localhost:8082";

    public QiuApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * @description: 调用接口
     * @params: [invokeParams]
     * @return: java.lang.Object
     * @author: qiu
     * @dateTime: 2024/3/23 13:01
     */
    public Object invokeInterface(InvokeParams invokeParams) {
        // 根据不同请求方法处理请求参数
        String method = invokeParams.getMethod();
        // 统一将请求参数转为JSON字符串
        String jsonStr = JSONUtil.toJsonStr(invokeParams.getRequestParams());
        log.info("请求参数转JSON字符串：{}", jsonStr);
        // GET请求
        if (method.equals("GET")) {
            return getMethod(jsonStr, invokeParams);
        }
        // POST请求
        if (method.equals("POST")) {
            return postMethod(jsonStr, invokeParams);
        }
        return "接口方法参数错误";
    }



    /**
     * @description: 检查接口调用状态
     * @params: [invokeParams]
     * @return: java.lang.Boolean
     * @author: qiu
     * @dateTime: 2024/3/22 22:24
     */
    public Boolean invokeInterfaceStatus(InvokeParams invokeParams) {
        String host = invokeParams.getHost();
        String url = invokeParams.getUrl();
        String params = invokeParams.getRequestParams();
        String method = invokeParams.getMethod();
        String jsonStr = JSONUtil.toJsonStr(params);
        if (method.equals("GET")) {
            JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
            Map<String, Object> map = new HashMap<>();
            for (String key : jsonObject.keySet()) {
                map.put(key, jsonObject.get(key));
            }
            HttpResponse response = HttpRequest.get(GATEWAY_HOST + url)
                    .addHeaders(getHeaderMap(host, jsonStr))
                    .form(map).execute();
            return response.isOk();
        }
        if (method.equals("POST")) {
            HttpResponse response = HttpRequest.post(GATEWAY_HOST + url)
                    .addHeaders(getHeaderMap(host, jsonStr))
                    .body(jsonStr).execute();
            return response.isOk();
        }
        return false;
    }

    /**
     * @description: GET请求处理方法
     * @params: [jsonStr, invokeParams]
     * @return: java.lang.Object
     * @author: qiu
     * @dateTime: 2024/3/23 12:32
     */
    private Object getMethod(String jsonStr, InvokeParams invokeParams) {
        // 将JSON字符串转为JSON对象
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        // 将JSON对象转为Map
        Map<String, Object> map = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.get(key));
        }
        log.info("GET请求参数：{}", map);
        // 请求地址+获取默认请求头Map+请求参数
        HttpResponse response = HttpRequest.get(GATEWAY_HOST + invokeParams.getUrl())
                .addHeaders(getHeaderMap(invokeParams.getHost(), jsonStr))
                .form(map).execute();
        return responseHandler(response);
    }

    /**
     * @description: POST请求处理方法
     * @params: [jsonStr, invokeParams]
     * @return: java.lang.Object
     * @author: qiu
     * @dateTime: 2024/3/23 13:00
     */
    private Object postMethod(String jsonStr, InvokeParams invokeParams) {
        log.info("POST请求参数：{}", jsonStr);
        // 请求地址+获取默认请求头Map+请求参数
        HttpResponse response = HttpRequest.post(GATEWAY_HOST + invokeParams.getUrl())
                .addHeaders(getHeaderMap(invokeParams.getHost(), jsonStr))
                .body(jsonStr).execute();
        return responseHandler(response);
    }

    /**
     * @description: 统一响应处理方法
     * @params: [response]
     * @return: java.lang.Object
     * @author: qiu
     * @dateTime: 2024/3/23 13:00
     */
    private Object responseHandler(HttpResponse response) {
        boolean isOk = response.isOk();
        String body = response.body();
        log.info("响应状态码：{}", isOk);
        log.info("响应结果：{}", body);
        // 如果请求失败打印错误信息，返回错误结果
        if (!isOk) {
            ResponseStatus status = ResponseStatus.getStatusByCode(response.getStatus());
            log.info("接口调用失败{}：{}", status.getStatus(), status.getDescription());
            return String.format("接口调用失败%s：%s", status.getStatus(), status.getDescription());
        }
        // 成功返回数据
        return body;
    }

    /**
     * @description: 获取默认请求头Map
     * @params: [body]
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author: qiu
     * @dateTime: 2024/3/15 0:11
     * 不能直接放发送密钥，必须加密，防止重放加入唯一随机数和时间戳
     */
    private Map<String, String> getHeaderMap(String host, String body) {
        String resultBody = body.replaceAll("[\\u4e00-\\u9fa5\\s]", "");
        Map<String, String> header = new HashMap<>();
        header.put("body", resultBody);
        header.put("hostAddr", host);
        header.put("accessKey", accessKey);
        header.put("nonce", RandomUtil.randomNumbers(5));
        header.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        header.put("sign", SignUtils.getSign(resultBody, secretKey));
        return header;
    }

}
