package com.URLShortener.worker.configuration;

import com.URLShortener.worker.RabbitMQListener;
import com.URLShortener.worker.repository.ShortURLRepository;
import com.URLShortener.worker.services.RabbitMQPublisherService;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class RabbitConfig {
    @Bean
    public Queue metrics() {
        return new Queue(VALIDATION_RESP_QUEUE_NAME);
    }

    public static final String VALIDATION_JOB_QUEUE_NAME = "metric_job";
    public static final String VALIDATION_RESP_QUEUE_NAME = "metric_resp";

    private final JdbcTemplate jdbc;
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };

    public RabbitConfig(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Bean
    public RabbitMQListener receiver() {
        return new RabbitMQListener(new RabbitMQPublisherService(), new ShortURLRepository(jdbc));
    }
}
