package com.github.ryanreymorris.orderescortbot.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Customer service call request.
 */
@Entity(name = "ServiceCallRequest")
@Table(name = "service_call_request")
@Data
public class ServiceCallRequest {

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
