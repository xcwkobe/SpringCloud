package com.xcw.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @class: ContextConfig
 * @author: ChengweiXing
 * @description: TODO
 **/
@Configuration
public class ContextConfig {

    @Bean
    @LoadBalanced//使用@LoadBalanced注解赋予RestTemplate负载均衡的能力
    public RestTemplate getResTemplate() {
        return new RestTemplate();
    }
}
