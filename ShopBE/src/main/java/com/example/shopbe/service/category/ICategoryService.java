package com.example.shopbe.service.category;

import com.example.shopbe.models.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    Category getCategoryById(Long id);
    Optional<Category> getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategory(Long id);

}
