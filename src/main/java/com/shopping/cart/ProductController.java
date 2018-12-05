package com.shopping.cart;

import com.shopping.cart.exception.ProductNotFoundException;
import com.shopping.cart.model.Product;
import com.shopping.cart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAll() {
        List<Product> products = this.productService.findAll();
        HttpStatus status = products != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity(
                products, status);
    }

    @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findById(@PathVariable final Long id) {
        Product product = null;
        try {
            product = this.productService.findById(id);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        HttpStatus status = product != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity(
         product, status);
    }

    @PostMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody Product product) {
        Long id = this.productService.create(product);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created (location).build();
    }

    @PutMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@RequestBody Product product) {

        try {
            this.productService.update(product);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("OK",HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable final Long id) {
        this.productService.remove(id);
    }
}
