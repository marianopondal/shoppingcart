package com.shopping.cart.service;

import com.shopping.cart.exception.ProductNotFoundException;
import com.shopping.cart.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(Long id) throws ProductNotFoundException;

    Long create(Product product);

    Long update(Product product) throws ProductNotFoundException;

    void remove(Long id);

}
