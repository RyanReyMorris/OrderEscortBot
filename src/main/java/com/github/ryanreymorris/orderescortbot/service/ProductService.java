package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Product;

/**
 * Service for {@link Product}.
 */
public interface ProductService {

    void saveProduct(Product product);

    Product findProductById(Long id);

    String createCaption(Product product);

    void initProducts();
}
