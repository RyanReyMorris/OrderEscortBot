package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.mail.RussianMailItem;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

@Service
public class RussianMailService {

    private static final String URL = "https://delivery.pochta.ru/v2/calculate/tariff?json&object=27030&from=628484&to={0}&weight=2000&pack=10";

    private final RestTemplate restTemplate;

    public RussianMailService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public ResponseEntity<RussianMailItem> getPostsPlainJSON(String toIndex) {
        String url = MessageFormat.format(URL, toIndex);
        return this.restTemplate.getForEntity(url, RussianMailItem.class);
    }
}
