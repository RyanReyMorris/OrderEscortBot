package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

/**
 * Service for {@link Customer}.
 */
public interface CustomerService {

    /**
     * Save customer to db.
     *
     * @param customer - Customer.
     */
    void save(Customer customer);

    /**
     * Find customer by id.
     *
     * @param id - id of customer.
     */
    Customer findById(Long id);

    /**
     * Find less busy operator by his tec-support request count.
     *
     * @return optional of Customer.
     */
    Optional<Customer> findLessBusyTecSupport();

    /**
     * Create new customer by telegram message.
     *
     * @param message - telegram message.
     */
    void create(Message message);

    /**
     * Check if customer with following id already exist in db.
     *
     * @param id - customer id.
     * @return {@code true} if exist, else {@code false}..
     */
    boolean checkIfCustomerIsNew(Long id);
}
