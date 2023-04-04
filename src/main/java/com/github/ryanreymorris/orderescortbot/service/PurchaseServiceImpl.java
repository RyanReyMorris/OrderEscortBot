package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Purchase;
import com.github.ryanreymorris.orderescortbot.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link PurchaseService}.
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository repository;

    @Override
    public void save(Purchase purchase) {
        repository.save(purchase);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Purchase> findAllByPerformerId(Long performerId) {
        return repository.findAllByPerformerId(performerId);
    }

    @Override
    public List<Purchase> findAllByAuthorId(Long authorId) {
        return repository.findAllByAuthorId(authorId);
    }

    @Transactional
    @Override
    public void deleteAllByAuthorId(Long authorId) {
        repository.removeAllByAuthorId(authorId);
    }
}
