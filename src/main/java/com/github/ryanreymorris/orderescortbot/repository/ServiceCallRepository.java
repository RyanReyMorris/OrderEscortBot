package com.github.ryanreymorris.orderescortbot.repository;

import com.github.ryanreymorris.orderescortbot.entity.ServiceCall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * {@link ServiceCall} repository.
 */
public interface ServiceCallRepository extends JpaRepository<ServiceCall, Long> {

    List<ServiceCall> findAllByPerformerId(Long performerId);
}
