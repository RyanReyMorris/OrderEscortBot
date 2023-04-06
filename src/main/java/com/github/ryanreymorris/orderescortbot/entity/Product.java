package com.github.ryanreymorris.orderescortbot.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model that imitates products in e-shop db.
 */
@Entity(name = "Product")
@Table(name = "product")
@Data
public class Product {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Long price;

    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "seller")
    private String seller;
}
