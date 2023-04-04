package com.github.ryanreymorris.orderescortbot.entity;

import javax.persistence.*;

import lombok.Data;

@Entity(name = "ServiceCall")
@Table(name = "service_call")
@Data
public class ServiceCall {

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Customer author;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "performer_id", referencedColumnName = "id")
    private Customer performer;
}
