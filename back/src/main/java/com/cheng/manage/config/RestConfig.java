package com.cheng.manage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name RestConfig
 * @date 2021/12/8 23:38
 */
@Configuration
public class RestConfig {
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //连接20秒超时
        requestFactory.setConnectTimeout(60*1000);
        //读数20秒超时
        requestFactory.setReadTimeout(20*1000);
        return new RestTemplate(requestFactory);
    }
}
