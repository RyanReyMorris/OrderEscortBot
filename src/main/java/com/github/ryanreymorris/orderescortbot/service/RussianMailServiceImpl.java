package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.mail.RussianMailItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

@Service
public class RussianMailServiceImpl implements RussianMailService {
    /**
     * Url of russian mail group.
     */
    private static final String URL = "https://delivery.pochta.ru/v2/calculate/tariff?json&object=27030&from={0}&to={1}&weight=2000&pack=10";

    @Value("${departure.index}")
    private String departureIndex;

    private final RestTemplate restTemplate;

    public RussianMailServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * {@inheritDoc}
     */
    public ResponseEntity<RussianMailItem> getPackageTransferCost(String destinationIndex) {
        String url = MessageFormat.format(URL, departureIndex, destinationIndex);
        return this.restTemplate.getForEntity(url, RussianMailItem.class);
    }
}
