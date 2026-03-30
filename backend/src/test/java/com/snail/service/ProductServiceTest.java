package com.snail.service;

import com.snail.dto.ProductDTO;
import com.snail.entity.Product;
import com.snail.repository.ProductRepository;
import com.snail.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        product = new Product();
        product.setId(1L);
        product.setName("测试商品");
        product.setDescription("这是一个测试商品");
        product.setPrice(new BigDecimal("99.99"));
        product.setStock(10);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("测试商品");
        productDTO.setDescription("这是一个测试商品");
        productDTO.setPrice(new BigDecimal("99.99"));
        productDTO.setStock(10);
        productDTO.setIsActive(true);
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("测试商品", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.createProduct(productDTO);

        assertNotNull(result);
        assertEquals("测试商品", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.updateProduct(1L, productDTO);

        assertNotNull(result);
        assertEquals("测试商品", result.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProducts() {
        List<Product> productList = Arrays.asList(product);
        Page<Product> productPage = new PageImpl<>(productList, PageRequest.of(0, 10), 1);
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(productPage);

        var result = productService.getProducts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(productRepository, times(1)).findAll(any(PageRequest.class));
    }
}