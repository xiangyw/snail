package com.snail.controller;

import com.snail.dto.ProductDTO;
import com.snail.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testGetProducts() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("测试商品");
        product.setPrice(new BigDecimal("99.99"));
        product.setStock(10);

        List<ProductDTO> productList = Arrays.asList(product);
        Page<ProductDTO> productPage = new PageImpl<>(productList);

        when(productService.getProducts(any(PageRequest.class))).thenReturn(productPage);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("测试商品"));
    }

    @Test
    void testGetProductById() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("测试商品");
        product.setPrice(new BigDecimal("99.99"));

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("测试商品"));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDTO inputProduct = new ProductDTO();
        inputProduct.setName("新商品");
        inputProduct.setPrice(new BigDecimal("199.99"));
        inputProduct.setStock(5);

        ProductDTO savedProduct = new ProductDTO();
        savedProduct.setId(1L);
        savedProduct.setName("新商品");
        savedProduct.setPrice(new BigDecimal("199.99"));
        savedProduct.setStock(5);

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(savedProduct);

        String productJson = "{ \"name\": \"新商品\", \"price\": 199.99, \"stock\": 5 }";

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("新商品"))
                .andExpect(jsonPath("$.price").value(199.99));
    }
}