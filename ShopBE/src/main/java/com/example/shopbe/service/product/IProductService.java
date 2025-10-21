package com.example.shopbe.service.product;

import com.example.shopbe.models.Product;
import com.example.shopbe.request.AddProductRequest;
import com.example.shopbe.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    Product updateProduct(UpdateProductRequest product, Long id);
    void deleteProduct(Long id);

    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);



 }
