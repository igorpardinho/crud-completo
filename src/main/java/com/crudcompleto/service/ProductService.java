package com.crudcompleto.service;

import com.crudcompleto.models.ProductModel;
import com.crudcompleto.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductService {

    public final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductModel> findAll(){
        return productRepository.findAll();
    }

    public ProductModel save(ProductModel productModel){
        return productRepository.save(productModel);
    }

    public ProductModel findByName(String name){
        return productRepository.findByName(name);
    }
}
