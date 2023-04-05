package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Purchase;

import java.util.List;

/**
 * Service for {@link Purchase}.
 */
public interface PurchaseService {

    /**
     * Save purchase to db.
     *
     * @param purchase purchase.
     */
    void save(Purchase purchase);

    /**
     * Find list of purchases by performer.
     *
     * @param performerId id of performer.
     * @return list of purchases by performer.
     */
    List<Purchase> findAllByPerformerId(Long performerId);

    /**
     * Find list of purchases by author.
     *
     * @param authorId id of author.
     * @return list of purchases by author.
     */
    List<Purchase> findAllByAuthorId(Long authorId);

    /**
     * Delete list of purchases by author.
     *
     * @param authorId id of author.
     */
    void deleteAllByAuthorId(Long authorId);
}
