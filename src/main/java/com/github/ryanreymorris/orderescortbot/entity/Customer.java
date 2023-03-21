package com.github.ryanreymorris.orderescortbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Entity(name = "Customer")
@Table(name = "customer")
@Getter
@ToString
@RequiredArgsConstructor
public class Customer {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "is_satisfied")
    private boolean isSatisfied;

    @Column(name = "is_admin")
    private boolean isAdmin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public boolean isSatisfied() {
        return isSatisfied;
    }

    public void setSatisfied(boolean satisfied) {
        isSatisfied = satisfied;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return isSatisfied == customer.isSatisfied && isAdmin == customer.isAdmin && Objects.equals(id, customer.id) && Objects.equals(chatId, customer.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, isSatisfied, isAdmin);
    }
}
