package com.shopping_app.microservices.product.controller;

import com.shopping_app.microservices.product.dto.ProductRequest;
import com.shopping_app.microservices.product.dto.ProductResponse;
import com.shopping_app.microservices.product.model.Product;
import com.shopping_app.microservices.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        // We can implement timeout feature here to check for circuit breaker functionality
        return productService.getAllProducts();
    }
}
