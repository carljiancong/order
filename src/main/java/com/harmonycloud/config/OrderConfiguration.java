package com.harmonycloud.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfiguration {
    @Bean
    @ConditionalOnMissingBean(OrderConfigurationProperties.class)
    public OrderConfigurationProperties configProperties() {
        return new OrderConfigurationProperties();
    }
}