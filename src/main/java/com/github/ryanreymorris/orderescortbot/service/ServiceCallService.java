package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.ServiceCall;

import java.util.List;

/**
 * Service for {@link ServiceCall}.
 */
public interface ServiceCallService {

    void save(ServiceCall serviceCall);

    void delete(Long authorId);

    List<ServiceCall> findAllByPerformerId(Long performerId);
}
