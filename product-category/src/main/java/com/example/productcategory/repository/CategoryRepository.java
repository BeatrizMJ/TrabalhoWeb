package com.example.productcategory.repository;

import com.example.productcategory.model.Category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByName(String name); 

    Long countProductsById(Long categoryId); 
    
}
