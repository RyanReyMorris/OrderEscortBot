package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.ServiceCall;
import com.github.ryanreymorris.orderescortbot.repository.ServiceCallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link ServiceCallService}.
 */
@Service
public class ServiceCallServiceImpl implements ServiceCallService {

    @Autowired
    private ServiceCallRepository repository;

    @Override
    public void save(ServiceCall serviceCall) {
        repository.save(serviceCall);
    }

    @Override
    public void delete(Long authorId) {
        repository.deleteById(authorId);
    }

    @Override
    public List<ServiceCall> findAllByPerformerId(Long performerId) {
        return repository.findAllByPerformerId(performerId);
    }
}
