package com.example.shopbe.service.category;

import com.example.shopbe.exceptions.CategoryNotFoundException;
import com.example.shopbe.exceptions.ResourceNotFound;
import com.example.shopbe.models.Category;
import com.example.shopbe.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->new CategoryNotFoundException("Category not found!"));
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findByName(name))
                .orElseThrow(() -> new ResourceNotFound("not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c ->!categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new CategoryNotFoundException("Already existing category!"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCat -> { oldCat.setName(category.getName());
                return categoryRepository.save(oldCat);}
                ).orElseThrow(() -> new CategoryNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,
                        () ->{throw new CategoryNotFoundException("Category not found!");});
    }
}
