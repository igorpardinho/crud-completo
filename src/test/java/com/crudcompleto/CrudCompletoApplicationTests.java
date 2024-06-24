package com.crudcompleto;


import com.crudcompleto.models.ProductModel;
import com.crudcompleto.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrudCompletoApplicationTests {


    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ProductService productService;


    @Test
    void testCreateSucessProduct() {
        ProductModel  productModel = new ProductModel("test", new BigDecimal(100));
        webTestClient
                .post()
                .uri("/products")
                .bodyValue(productModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(productModel.getName())
                .jsonPath("$.value").isEqualTo(productModel.getValue());
    }

    @Test
    void testCreateFailureProduct(){
        webTestClient
                .post()
                .uri("/products")
                .bodyValue(new ProductModel("", new BigDecimal(0)))
                .exchange()
                .expectStatus().isBadRequest();

    }
    @Test
    void testUpdateProduct(){

        ProductModel productModel = new ProductModel("teste",new BigDecimal(10));

        ProductModel saveProduct = productService.save(productModel);

        ProductModel updateProduct = new ProductModel("testeeee",new BigDecimal(99));

        webTestClient
                .put()
                .uri("/products/{id}", Collections.singletonMap("id",saveProduct.getId()))
                .body(Mono.just(updateProduct),ProductModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(updateProduct.getName())
                .jsonPath("$.value").isEqualTo(updateProduct.getValue());

    }
    @Test
    void testDeleteProduct(){

        ProductModel productModel = new ProductModel("teste",new BigDecimal(10));

        ProductModel saveProduct = productService.save(productModel);

        webTestClient
                .delete().uri("/products/{id}",Collections.singletonMap("id",saveProduct.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody();

    }

}
