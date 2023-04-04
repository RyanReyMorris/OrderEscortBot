package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link Customer}.
 */
public interface CustomerService {

    void saveCustomer(Customer Customer);

    Customer findCustomerById(Long id);

    Optional<Customer> findLessBusyTecSupport();

    void createNewCustomer(Message message);

    boolean checkIfCustomerIsNew(Long id);
}
