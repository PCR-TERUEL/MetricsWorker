package com.URLShortener.worker;

import com.URLShortener.worker.domain.ShortURL;
import com.URLShortener.worker.domain.Work;
import com.URLShortener.worker.repository.ShortURLRepository;
import com.URLShortener.worker.services.RabbitMQPublisherService;
import org.junit.After;
import org.junit.Before;
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


@SpringBootTest
@RabbitListener(queues = "validation_resp")
class MetricsWorkerTests {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private final RabbitMQPublisherService rabbitMQPublisherService;


    String resp;

    @Autowired
    JdbcTemplate jdbc;
    MetricsWorkerTests() {
        rabbitMQPublisherService = new RabbitMQPublisherService();
    }


    @Before
    void before(){
        resp = "";
    }
    @After
    void after(){
        resp = "";
    }
    @Test
    void getsAnEmptyMetricJob() throws InterruptedException {
        this.template.convertAndSend("validation_job", "{\"idUser\":\"2\"}");
        Thread.sleep(1000);

        assert(resp.equals("{\"idUser\":\"2\",\"metrics\":[]}"));
    }

    @Test
    void getsAFilledMetricJob() throws InterruptedException, URISyntaxException {

        new ShortURLRepository(jdbc).save(new ShortURL("hash", "target", new URI(""), "sponsor",
                new Date(1), new Date(1), 2L, 1, true, "ip",
                "country"));

        this.template.convertAndSend("metric_job", "{\"idUser\":\"2\"}");
        Thread.sleep(1000);
        assert(resp.equals("{\"idUser\":\"2\",\"metrics\":[{\"valid\":true,\"shortedUrl\":\"09bb6428\",\"clicks\":0,\"url\":\"https:\\/\\/www.live.com\"},{\"valid\":true,\"shortedUrl\":\"hash\",\"clicks\":0,\"url\":\"target\"}]}"));
    }

    @RabbitHandler
    public void receive(String in) {
        resp = in;
    }
}
