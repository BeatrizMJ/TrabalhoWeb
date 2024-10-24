package com.example.productcategory.service;

import com.example.productcategory.model.Product;
import com.example.productcategory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

   
    public List<Product> findByCategoryName(String categoryName) {
        return productRepository.findByCategories_Name(categoryName);
    }

    public List<Product> findByPriceLessThan(Double price) {
        return productRepository.findByPriceLessThan(price);
    }
}