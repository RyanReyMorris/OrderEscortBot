package com.github.ryanreymorris.orderescortbot.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Customer purchase model.
 */
@Entity(name = "Purchase")
@Table(name = "purchase")
@Data
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer author;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "performer_id", referencedColumnName = "id")
    private Customer performer;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "count")
    private Integer count;
}
