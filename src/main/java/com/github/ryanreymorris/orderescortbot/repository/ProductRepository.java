package com.github.ryanreymorris.orderescortbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.ryanreymorris.orderescortbot.entity.Product;

/**
 * {@link Product} repository.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
