package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.ServiceCallRequest;

import java.util.List;

/**
 * Service for {@link ServiceCallRequest}.
 */
public interface ServiceCallService {

    /**
     * Save serviceCallRequest to db.
     *
     * @param serviceCallRequest serviceCallRequest.
     */
    void save(ServiceCallRequest serviceCallRequest);

    /**
     * Delete serviceCallRequest from db.
     *
     * @param authorId id of author.
     */
    void delete(Long authorId);

    /**
     * Find all serviceCallRequests from db by performer id.
     *
     * @param performerId id of performer.
     */
    List<ServiceCallRequest> findAllByPerformerId(Long performerId);
}
