package com.URLShortener.worker;

import com.URLShortener.worker.configuration.RabbitConfig;
import com.URLShortener.worker.domain.ShortURL;
import com.URLShortener.worker.domain.Work;
import com.URLShortener.worker.repository.ShortURLRepository;
import com.URLShortener.worker.services.RabbitMQPublisherService;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;


import java.net.URI;
import java.net.URISyntaxException;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@RabbitListener(queues = RabbitConfig.METRIC_RESP_QUEUE_NAME)
class MetricsWorkerTests {

    @Autowired
    private RabbitTemplate template;

    static String resp;

    @Autowired
    JdbcTemplate jdbc;


    @BeforeEach
    void before(){
        resp = "";
    }

    @Test
    void getsAnEmptyMetricJob() throws InterruptedException {

        this.template.convertAndSend(RabbitConfig.METRIC_JOB_QUEUE_NAME, "{\"idUser\":\"1\"}");
        Thread.sleep(1000);

        assert(resp.equals("{\"idUser\":\"1\",\"metrics\":[]}"));
    }

    @Test
    void getsAFilledMetricJob() throws InterruptedException, URISyntaxException {

        new ShortURLRepository(jdbc).save(new ShortURL("hash", "target", new URI(""), "sponsor",
                new Date(1), new Date(1), 2L, 1, true, "ip",
                "country"));

        this.template.convertAndSend(RabbitConfig.METRIC_JOB_QUEUE_NAME, "{\"idUser\":\"2\"}");
        Thread.sleep(1000);
        assertEquals(resp, "{\"idUser\":\"2\",\"metrics\":[{\"valid\":true,\"shortedUrl\":\"09bb6428\",\"clicks\"" +
                ":0,\"url\":\"https:\\/\\/www.live.com\"},{\"valid\":true,\"shortedUrl\":\"hash\",\"clicks\":0,\"url\":\"target\"}]}");
    }

    @RabbitHandler
    public void receive(String in) {
        resp = in;
    }
}
