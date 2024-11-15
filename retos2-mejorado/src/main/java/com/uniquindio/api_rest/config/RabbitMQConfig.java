package com.uniquindio.api_rest.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue logQueue() {
        return new Queue("CreateLogQueue", true);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue("notificationQueue", true);
    }
}
