package com.zosh.controller;


import com.zosh.exception.ProductException;
import com.zosh.model.Product;
import com.zosh.request.CreateProductRequest;
import com.zosh.response.ApiResponse;
import com.zosh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Product> createProduct (@RequestBody CreateProductRequest req) {
        Product product = productService.createProduct(req);

        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }


    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws ProductException {
        productService.deleteProduct(productId);
        ApiResponse res = new ApiResponse();
        res.setMessage("product deleted Successfully");
        res.setStatus(true);

        return  new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAllProduct() {
        List<Product> products = productService.findAllProducts();

        return new ResponseEntity<> (products, HttpStatus.OK);

    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct ( @RequestBody Product req, @PathVariable Long productId) throws ProductException {
        Product product = productService.updateProduct(productId, req );
        return new ResponseEntity<Product> (product, HttpStatus.CREATED);
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody CreateProductRequest[] req) {
        for ( CreateProductRequest product: req) {
            productService.createProduct(product);
        }

        ApiResponse res = new ApiResponse();
        res.setMessage("product deleted successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }








}