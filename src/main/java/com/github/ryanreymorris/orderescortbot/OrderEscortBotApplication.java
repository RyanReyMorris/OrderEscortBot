package com.github.ryanreymorris.orderescortbot;

import com.github.ryanreymorris.orderescortbot.entity.mail.RussianMailItem;
import com.github.ryanreymorris.orderescortbot.service.ProductService;
import com.github.ryanreymorris.orderescortbot.service.RussianMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
public class OrderEscortBotApplication implements ApplicationRunner {

    @Autowired
    private ProductService productService;

    @Autowired
    private RussianMailService mailService;

    public static void main(String[] args) {
        SpringApplication.run(OrderEscortBotApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        productService.initProducts();
    }
}

