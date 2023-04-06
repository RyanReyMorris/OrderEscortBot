package com.github.ryanreymorris.orderescortbot.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Customer contact information.
 */
@Entity(name = "ContactInfo")
@Table(name = "contact_info")
@Data
public class ContactInfo {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    /**
     * Meta-info about customer such as profile photo etc.
     */
    @Column(name = "v_card")
    private String vCard;
}
