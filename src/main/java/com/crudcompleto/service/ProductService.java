package com.crudcompleto.service;

import com.crudcompleto.models.ProductModel;
import com.crudcompleto.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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

    public Optional<ProductModel> findOne(UUID id){
       return productRepository.findById(id);
    }

    public void deleteProduct(ProductModel productModel){
         productRepository.delete(productModel);
    }
}
