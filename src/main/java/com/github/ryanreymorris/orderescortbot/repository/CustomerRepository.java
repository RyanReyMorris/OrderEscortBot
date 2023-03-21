package com.github.ryanreymorris.orderescortbot.repository;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAllByIsAdminIsTrue();


}
