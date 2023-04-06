package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.ServiceCallRequest;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(ServiceCallRequest serviceCallRequest) {
        repository.save(serviceCallRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long authorId) {
        repository.deleteById(authorId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceCallRequest> findAllByPerformerId(Long performerId) {
        return repository.findAllByPerformerId(performerId);
    }
}
