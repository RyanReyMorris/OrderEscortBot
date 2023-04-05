package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Customer;
import com.github.ryanreymorris.orderescortbot.repository.CustomerRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Implementation of {@link CustomerService} interface.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Override
    public void save(Customer customer) {
        repository.save(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer findById(Long id) {
        return repository.findById(id).
                orElseThrow(() -> new RuntimeException(MessageFormat.format("Покупателя с id = {0} не было найдено", id)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Customer> findLessBusyTecSupport() {
        List<Customer> performers = repository.findLessBusyTecSupport();
        if (performers.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(performers.get(new Random().nextInt(performers.size())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Message message) {
        User user = message.getFrom();
        Customer customer = new Customer();
        customer.setUserName(user.getUserName());
        customer.setName(getCustomerName(user));
        customer.setId(user.getId());
        repository.save(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfCustomerIsNew(Long id) {
        return repository.findById(id).isEmpty();
    }

    /**
     * Get available customer name.
     *
     * @param user - telegram user.
     * @return username.
     */
    private String getCustomerName(User user) {
        String customerName;
        String customerFirstName = user.getFirstName();
        String customerLastName = user.getLastName();
        if (StringUtils.isNoneEmpty(customerFirstName, customerLastName)) {
            customerName = MessageFormat.format("{0} {1}", customerFirstName, customerLastName);
        } else if (StringUtils.isNotEmpty(customerFirstName)) {
            customerName = customerFirstName;
        } else if (StringUtils.isNotEmpty(customerLastName)) {
            customerName = customerLastName;
        } else {
            customerName = user.getUserName();
        }
        return customerName;
    }
}
