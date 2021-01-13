package com.URLShortener.worker.configuration;

import com.URLShortener.worker.services.RabbitMQPublisherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitSenderConfiguration {

    @Bean
    public Queue tasks() {
        return new Queue(RabbitConfig.METRIC_RESP_QUEUE_NAME);
    }
    @Bean
    public RabbitMQPublisherService rabbitMQPublisherService(){return new RabbitMQPublisherService();}
}
