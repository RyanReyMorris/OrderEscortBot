package com.github.ryanreymorris.orderescortbot.repository;

import com.github.ryanreymorris.orderescortbot.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * {@link Purchase} repository.
 */
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findAllByPerformerId(Long performerId);

    List<Purchase> findAllByAuthorId(Long authorId);

    void removeAllByAuthorId(Long authorId);
}
