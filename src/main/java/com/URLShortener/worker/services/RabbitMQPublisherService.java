package com.URLShortener.worker.services;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class RabbitMQPublisherService {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue tasks;

    public void send(String message) {
        this.template.convertAndSend(tasks.getName(), message);
        System.out.println();
        System.out.println(" [x] Sent "+ tasks.getName() + "'" + message + "'");
    }
}
