package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface CustomerService {

    List<Customer> findAdmins();

    void saveCustomer(Customer Customer);

    Customer findCustomerById(Long id);

    Customer createNewCustomer(Message message);

    boolean checkIfCustomerIsNew(Long id);
}
