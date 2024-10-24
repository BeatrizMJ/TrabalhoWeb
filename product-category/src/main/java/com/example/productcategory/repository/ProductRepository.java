package com.example.productcategory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.productcategory.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    
    List<Product> findByCategories_Name(String name);

    
    List<Product> findByPriceLessThan(Double price);
}
