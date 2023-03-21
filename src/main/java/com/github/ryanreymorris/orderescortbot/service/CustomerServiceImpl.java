package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.MessageFormat;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository repository;

    @Override
    public List<Customer> findAdmins() {
        return repository.findAllByIsAdminIsTrue();
    }

    @Override
    public void saveCustomer(Customer customer) {
        repository.save(customer);
    }

    @Override
    public Customer findCustomerById(Long id) {
        return repository.findById(id).
                orElseThrow(() -> new RuntimeException(MessageFormat.format("Покупателя с id = {0} не было найдено", id)));
    }

    @Override
    public Customer createNewCustomer(Message message) {
        Customer customer = new Customer();
        customer.setId(message.getFrom().getId());
        customer.setChatId(message.getChatId());
        customer.setSatisfied(true);
        customer.setAdmin(false);
        repository.save(customer);
        return customer;
    }

    @Override
    public boolean checkIfCustomerIsNew(Long id) {
        return repository.findById(id).isEmpty();
    }
}
