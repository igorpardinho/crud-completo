package com.crudcompleto.controllers;

import com.crudcompleto.dtos.ProductDTO;
import com.crudcompleto.models.ProductModel;
import com.crudcompleto.repositories.ProductRepository;
import com.crudcompleto.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class ProductController {

    public final ProductRepository productRepository;
    public final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }


    @PostMapping("/products")
    private ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductDTO productDTO) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productDTO, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productModel));
    }

    @GetMapping("/products")
    private ResponseEntity<List<ProductModel>> getProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll());
    }

    @PutMapping("/products/{id}")
    private ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                 @RequestBody @Valid ProductDTO productDTO) {
        Optional<ProductModel> optionalProductModel = productRepository.findById(id);
        if (optionalProductModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productDTO, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productService.save(productModel));
    }

    @DeleteMapping("/products/{id}")
    private ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> optionalProductModel = productRepository.findById(id);
        if (optionalProductModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        productRepository.delete(optionalProductModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso");

    }

}
