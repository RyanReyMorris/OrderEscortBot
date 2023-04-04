package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Purchase;

import java.util.List;

/**
 * Service for {@link Purchase}.
 */
public interface PurchaseService {

    void save(Purchase purchase);

    void delete(Long id);

    List<Purchase> findAllByPerformerId(Long performerId);

    List<Purchase> findAllByAuthorId(Long authorId);

    void deleteAllByAuthorId(Long authorId);
}
