package com.github.ryanreymorris.orderescortbot;

import com.github.ryanreymorris.orderescortbot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring-application main class.
 */
@SpringBootApplication
public class OrderEscortBotApplication implements ApplicationRunner {

    @Autowired
    private ProductService productService;

    public static void main(String[] args) {
        SpringApplication.run(OrderEscortBotApplication.class, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(ApplicationArguments args) {
        productService.initProducts();
    }
}

