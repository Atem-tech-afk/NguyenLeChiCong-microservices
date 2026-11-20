package com.example.menu_service.service;

import com.example.menu_service.entity.Category;
import com.example.menu_service.entity.MenuItem;
import com.example.menu_service.repository.CategoryRepository;
import com.example.menu_service.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<MenuItem> getAll() {
        return menuItemRepository.findAll();
    }

    public Optional<MenuItem> getById(Long id) {
        return menuItemRepository.findById(id);
    }

    public List<MenuItem> getByCategoryId(Long categoryId) {
        return menuItemRepository.findByCategoryId(categoryId);
    }

    public MenuItem create(MenuItem menuItem) {
        menuItem.setId(null);
        linkCategory(menuItem);
        return menuItemRepository.save(menuItem);
    }

    public Optional<MenuItem> update(Long id, MenuItem details) {
        return menuItemRepository.findById(id).map(existing -> {
            existing.setName(details.getName());
            existing.setPrice(details.getPrice());
            existing.setDescription(details.getDescription());
            existing.setImageUrl(details.getImageUrl());
            if (details.getIsAvailable() != null) {
                existing.setIsAvailable(details.getIsAvailable());
            }
            if (details.getCategory() != null) {
                existing.setCategory(details.getCategory());
                linkCategory(existing);
            }
            return menuItemRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (menuItemRepository.existsById(id)) {
            menuItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void linkCategory(MenuItem menuItem) {
        if (menuItem.getCategory() != null && menuItem.getCategory().getId() != null) {
            Category category = categoryRepository.findById(menuItem.getCategory().getId()).orElse(null);
            menuItem.setCategory(category);
        }
    }
}
