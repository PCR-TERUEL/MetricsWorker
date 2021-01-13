package com.URLShortener.worker;

import com.URLShortener.worker.configuration.RabbitConfig;
import com.URLShortener.worker.domain.Work;
import com.URLShortener.worker.repository.ShortURLRepository;
import com.URLShortener.worker.services.RabbitMQPublisherService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

@RabbitListener(queues = RabbitConfig.VALIDATION_JOB_QUEUE_NAME)
public class RabbitMQListener {

    private final ShortURLRepository shortURLRepository;

    @Autowired
    private final RabbitMQPublisherService rabbitMQPublisherService;

    public RabbitMQListener(RabbitMQPublisherService rabbitMQPublisherService, ShortURLRepository shortURLRepository) {
        this.rabbitMQPublisherService = rabbitMQPublisherService;
        this.shortURLRepository = shortURLRepository;
    }

    @RabbitHandler
    public void receive(String in) {
        System.out.println(" [x] Received METRIC'" + in + "'");
        new Work(in, rabbitMQPublisherService, shortURLRepository);
    }
}
