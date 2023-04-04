package com.github.ryanreymorris.orderescortbot.entity;

import javax.persistence.*;

import com.github.ryanreymorris.orderescortbot.handler.command.BotCommandEnum;
import lombok.Data;

@Entity(name = "Customer")
@Table(name = "customer")
@Data
public class Customer {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "last_message")
    private Integer lastMessage;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private ContactInfo contactInfo;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "is_satisfied")
    private boolean isSatisfied;

    @Column(name = "is_in_tec_sup_process")
    private boolean isInTecSupProcess;

    @Column(name = "is_service_call")
    private boolean isServiceCall;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_command")
    private BotCommandEnum currentCommand;
}
