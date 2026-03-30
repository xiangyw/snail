package com.snail.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductTest {

    @Test
    void testProductEntity() {
        Product product = new Product();
        product.setId(1L);
        product.setName("测试商品");
        product.setDescription("这是一个测试商品");
        product.setPrice(new BigDecimal("99.99"));
        product.setStock(10);
        product.setImageUrl("http://example.com/image.jpg");
        product.setCategoryId(1L);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, product.getId());
        assertEquals("测试商品", product.getName());
        assertEquals("这是一个测试商品", product.getDescription());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        assertEquals(Integer.valueOf(10), product.getStock());
        assertEquals("http://example.com/image.jpg", product.getImageUrl());
        assertEquals(Long.valueOf(1L), product.getCategoryId());
        assertTrue(product.getIsActive());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }
}