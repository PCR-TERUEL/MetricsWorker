package com.URLShortener.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;

public class RabbitAmqpRunner implements CommandLineRunner {

    @Value("${tutorial.client.duration:10}")
    private int duration;

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Override
    @Scheduled
    public void run(String... arg0) throws Exception {
        Thread.sleep(duration);

    }
}