package com.shopping.cart.service.impl;

import com.shopping.cart.exception.ProductNotFoundException;
import com.shopping.cart.model.Product;
import com.shopping.cart.repository.ProductRepository;
import com.shopping.cart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultProductService implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    public Product findById(Long id) throws ProductNotFoundException {
        Optional<Product> dbProduct = this.productRepository.findById(id);
        Product product = null;
        if (dbProduct.isPresent()) {
            product = dbProduct.get();
        } else {
            //TODO log and throw an Exception
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    public Long create(Product product) {
        Long id = this.productRepository.save(product).getId();
        return id;
    }

    public Long update(Product product) throws ProductNotFoundException {
        Long productId = product.getId();
        Product productToUpdate = this.findById(productId);
        productToUpdate.setName(product.getName());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setStock(product.getStock());
        this.productRepository.save(productToUpdate);
        return productId;
    }

    public void remove(Long id) {
        this.productRepository.deleteById(id);
    }
}
