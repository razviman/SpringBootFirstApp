package com.example.shopbe.controllers;


import com.example.shopbe.exceptions.ProductNotFoundException;
import com.example.shopbe.exceptions.ResourceNotFound;
import com.example.shopbe.models.Product;
import com.example.shopbe.request.AddProductRequest;
import com.example.shopbe.request.UpdateProductRequest;
import com.example.shopbe.response.APIresponse;
import com.example.shopbe.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<APIresponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new APIresponse("Found", products));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<APIresponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new APIresponse("Found", product));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIresponse(e.getMessage(), null));

        }
    }


    @PostMapping("/add")
    public ResponseEntity<APIresponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product addedProduct = productService.addProduct(product);
            return ResponseEntity.ok(new APIresponse("Added", addedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIresponse(e.getMessage(), INTERNAL_SERVER_ERROR));
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIresponse> updateProduct(@RequestBody UpdateProductRequest product,@PathVariable Long id) {
        try {
            Product updatedProduct = productService.updateProduct(product, id);
            return ResponseEntity.ok(new APIresponse("Updated", updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIresponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIresponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new APIresponse("Deleted", id));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIresponse(e.getMessage(), null));
        }
    }

}
