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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/")
public class ProductController {


    public final ProductService productService;



    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductDTO productDTO) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productDTO, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> productsList = productService.findAll();
        if (!productsList.isEmpty()) {
            for (ProductModel productModel:productsList){
                UUID id = productModel.getId();
                productModel.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> optionalProductModel = productService.findOne(id);
        if (optionalProductModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        ProductModel productModel = optionalProductModel.get();
        productModel.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(productModel);
    }


    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
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
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> optionalProductModel = productService.findOne(id);
        if (optionalProductModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        productService.deleteProduct(optionalProductModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso");

    }

}
