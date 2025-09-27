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
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize
    ) {
        // --- NEW LOG 1: Log the incoming request parameters ---
        System.out.println("--- REQUEST RECEIVED AT /api/products ---");
        System.out.println("Category received: " + category);

        Page<Product> res = productService.getAllProduct(
                category, color, size, minPrice, maxPrice,
                minDiscount, sort, stock, pageNumber, pageSize);

        // --- NEW LOG 2: Log the number of products being returned ---
        System.out.println("Backend is returning " + res.getContent().size() + " products.");
        System.out.println("----------------------------------------");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}