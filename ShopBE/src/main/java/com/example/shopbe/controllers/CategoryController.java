package com.example.shopbe.controllers;

import com.example.shopbe.exceptions.ResourceNotFound;
import com.example.shopbe.models.Category;
import com.example.shopbe.response.APIresponse;
import com.example.shopbe.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<APIresponse> getAllCategories(){
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new APIresponse("found", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIresponse("Error", INTERNAL_SERVER_ERROR));
        }

    }

    @PostMapping("/add")
    public ResponseEntity<APIresponse> addCategory(@RequestBody Category category){
        try {
            Category thecat = categoryService.addCategory(category);
            return ResponseEntity.ok(new APIresponse("Category added!", thecat));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<APIresponse> getCategoryById(@PathVariable Long id){
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new APIresponse("found", category));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIresponse("Error", NOT_FOUND));
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<APIresponse> getCategoryByName(@PathVariable String name){
        try {
            Category category = categoryService.getCategoryByName(name).orElseThrow(() -> new ResourceNotFound("Error"));
            return ResponseEntity.ok(new APIresponse("found", category));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIresponse("Error", NOT_FOUND));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIresponse>  deleteCategory(@PathVariable Long id){
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new APIresponse("deleted", null));
        }catch (Exception e){
            return ResponseEntity.status(NOT_FOUND).body(new APIresponse(e.getMessage(), null));
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<APIresponse> updateCategory(@PathVariable Long id, @RequestBody Category category){
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new APIresponse("updated", updatedCategory));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIresponse(e.getMessage(), null));
        }
    }



}
