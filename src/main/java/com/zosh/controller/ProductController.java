package com.zosh.controller;

import com.zosh.exception.ProductException;
import com.zosh.model.Product;
import com.zosh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> color,
            @RequestParam(required = false) List<String> size,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,

            // --- FIX APPLIED ---
            // pageNumber is now optional and defaults to 0
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            // pageSize is now optional and defaults to 10
            @RequestParam(defaultValue = "10", required = false) Integer pageSize
    ) {
        Page<Product> res = productService.getAllProduct(
                category, color, size, minPrice, maxPrice,
                minDiscount, sort, stock, pageNumber, pageSize);

        System.out.println("complete products");

        // --- BEST PRACTICE FIX APPLIED ---
        // Changed HttpStatus.ACCEPTED to HttpStatus.OK for successful GET request
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);

        // --- BEST PRACTICE FIX APPLIED ---
        // Changed HttpStatus.ACCEPTED to HttpStatus.OK for consistency
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}