package com.github.ryanreymorris.orderescortbot.repository;

import com.github.ryanreymorris.orderescortbot.entity.ServiceCallRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * {@link ServiceCallRequest} repository.
 */
public interface ServiceCallRepository extends JpaRepository<ServiceCallRequest, Long> {

    List<ServiceCallRequest> findAllByPerformerId(Long performerId);
}
