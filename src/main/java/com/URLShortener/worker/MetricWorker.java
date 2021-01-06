package com.URLShortener.worker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MetricWorker {


    @Bean
    public CommandLineRunner tutorial() {
        return new RabbitAmqpRunner();
    }
    public static void main(String[] args) {
        SpringApplication.run(MetricWorker.class, args);
    }


}
