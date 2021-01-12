package com.URLShortener.worker.domain;

import com.URLShortener.worker.repository.ShortURLRepository;
import com.URLShortener.worker.services.RabbitMQPublisherService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Work extends Thread{


    private final ShortURLRepository shortURLRepository;
    private MetricQueueMessage metricQueueMessage;
    private final RabbitMQPublisherService rabbitMQPublisherService;
    private String idUser;
    public Work(String input, RabbitMQPublisherService rabbitMQPublisherService, ShortURLRepository shortURLRepository) {
        this.rabbitMQPublisherService = rabbitMQPublisherService;
        this.shortURLRepository = shortURLRepository;

        JSONParser jsonParser = new JSONParser();

        try (StringReader reader = new StringReader(input))
        {
            Object obj = jsonParser.parse(reader);
            JSONObject message = (JSONObject) obj;

            this.idUser = (String) message.get(MetricQueueMessage.ID_USER_FIELD_NAME);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        start();
    }

    @Override
    public void run() {
        try {
            this.rabbitMQPublisherService.send(this.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        List<ShortURL> shortURLS = shortURLRepository.findByUser(this.idUser);
        List<Metric> m = new ArrayList<>();
        for(ShortURL url : shortURLS){
            m.add(new Metric(url.getTarget(), url.getHash(), Integer.parseInt(String.valueOf(url.getClicks())), url.isValidated()));
        }
        return new MetricQueueMessage(idUser, m).toString();
    }

}
