package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class CategoryDbInit {

    @Autowired
    private CategoryDAO categoryDAO;

    @PostConstruct
    private void postConstruct() {
        List<Category> categories = Arrays.asList(
            new Category(1, "corporate"),
            new Category(2, "matrimonial"),
            new Category(3, "civil"),
            new Category(4, "criminal")
        );

        // Check existence and insert only if the category is not already in the database
        categories.forEach(category -> {
            if (categoryDAO.getCategoryById(category.getCatID()) == null) {
                categoryDAO.saveCategory(category);
                System.out.println("Inserted category: " + category.getCaseType());
            } else {
                System.out.println("Category already exists: " + category.getCaseType());
            }
        });
    }
}
