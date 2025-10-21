package com.example.shopbe.service.product;

import com.example.shopbe.exceptions.ProductNotFoundException;
import com.example.shopbe.models.Category;
import com.example.shopbe.models.Product;
import com.example.shopbe.repository.category.CategoryRepository;
import com.example.shopbe.repository.product.ProductRepository;
import com.example.shopbe.request.AddCategoryRequest;
import com.example.shopbe.request.AddProductRequest;
import com.example.shopbe.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest product) {
        // check if the category exists, if not create a new one


        Category category = categoryRepository.findByName(product.getCategoryRequest().getCategory())
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(product.getCategoryRequest().getCategory());
                    categoryRepository.save(newCat);
                    return newCat;
                });

        return productRepository.save(createProduct(product, category));
    }

    private Product createProduct(AddProductRequest product, Category category) {
        return new Product(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getInventory(),
                product.getBrand(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public Product updateProduct(UpdateProductRequest product, Long id) {

        return productRepository.findById(id)
                .map(existingProd -> updateExistingProduct(existingProd, product))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct,  UpdateProductRequest updateProductRequest) {
        existingProduct.setName(updateProductRequest.getName());
        existingProduct.setDescription(updateProductRequest.getDescription());
        existingProduct.setPrice(updateProductRequest.getPrice());
        existingProduct.setInventory(updateProductRequest.getInventory());
        existingProduct.setBrand(updateProductRequest.getBrand());
        existingProduct.setCategory(updateProductRequest.getCategory());
        return existingProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                () ->{ throw new ProductNotFoundException("Product not found!");});
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
