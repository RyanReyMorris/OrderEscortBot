package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Product;

/**
 * Service for {@link Product}.
 */
public interface ProductService {

    /**
     * Save product to db.
     *
     * @param product - product
     */
    void save(Product product);

    /**
     * Find product by id.
     *
     * @param id - id.
     * @return product.
     */
    Product findById(Long id);

    /**
     * Create caption of product.
     *
     * @param product product.
     * @return caption for product.
     */
    String createCaption(Product product);

    /**
     * Initialize products from resource photos.
     */
    void initProducts();
}
