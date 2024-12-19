package com.qiu.apiclientsdk;

import com.qiu.apiclientsdk.client.QiuApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 接口调用客户端配置类
 * @className: QiuApiClientConfig.java
 * @author: qiu
 * @createTime: 2024/3/15 0:02
 */
@Configuration
@ConfigurationProperties("qiuapi.client")
@ComponentScan
@Data
public class QiuApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public QiuApiClient qiuApiClient() {
        return new QiuApiClient(accessKey, secretKey);
    }

}
