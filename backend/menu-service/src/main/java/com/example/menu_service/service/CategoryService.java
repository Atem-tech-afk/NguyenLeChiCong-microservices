package com.example.menu_service.service;

import com.example.menu_service.entity.Category;
import com.example.menu_service.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category create(Category category) {
        category.setId(null);
        return categoryRepository.save(category);
    }

    public Optional<Category> update(Long id, Category categoryDetails) {
        return categoryRepository.findById(id).map(existing -> {
            existing.setName(categoryDetails.getName());
            existing.setDescription(categoryDetails.getDescription());
            return categoryRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
