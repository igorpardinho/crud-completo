package com.crudcompleto.controllers;

import com.crudcompleto.dtos.ProductDTO;
import com.crudcompleto.models.ProductModel;
import com.crudcompleto.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class ProductController {

    public final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping("/products")
    private ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductDTO productDTO) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productDTO, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productModel));
    }

    @GetMapping("/products")
    private ResponseEntity<List<ProductModel>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll());
    }

    @GetMapping("/products/{id}")
    private ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> optionalProductModel = productService.findOne(id);
        return optionalProductModel.<ResponseEntity<Object>>map(productModel -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(productModel)).orElseGet(() -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Produto não encontrado"));
    }


    @PutMapping("/products/{id}")
    private ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                 @RequestBody @Valid ProductDTO productDTO) {
        Optional<ProductModel> optionalProductModel = productService.findOne(id);
        if (optionalProductModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        ProductModel productModel = optionalProductModel.get();
        BeanUtils.copyProperties(productDTO, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productService.save(productModel));
    }

    @DeleteMapping("/products/{id}")
    private ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> optionalProductModel = productService.findOne(id);
        if (optionalProductModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        productService.deleteProduct(optionalProductModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso");

    }

}
