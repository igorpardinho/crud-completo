package com.crudcompleto;

import com.crudcompleto.models.ProductModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrudCompletoApplicationTests {


    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateSucessProduct() {
        var productModel = new ProductModel("test", new BigDecimal(100));
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

}
