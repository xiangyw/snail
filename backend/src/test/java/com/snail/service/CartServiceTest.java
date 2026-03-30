package com.snail.service;

import com.snail.dto.AddToCartRequest;
import com.snail.dto.CartDTO;
import com.snail.dto.UpdateCartItemRequest;
import com.snail.entity.Cart;
import com.snail.entity.CartItem;
import com.snail.entity.Product;
import com.snail.repository.CartRepository;
import com.snail.repository.ProductRepository;
import com.snail.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 初始化测试数据
        cart = new Cart();
        cart.setId(1L);
        cart.setUserId(1L);
        cart.setItems(new ArrayList<>());
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        product = new Product();
        product.setId(1L);
        product.setName("测试商品");
        product.setPrice(new BigDecimal("99.99"));
        product.setStock(10);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setPrice(product.getPrice());
        cartItem.setCreatedAt(LocalDateTime.now());
        cartItem.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGetUserCart() {
        when(cartRepository.findOrCreateByUserId(1L)).thenReturn(cart);

        CartDTO result = cartService.getUserCart(1L);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getUserId());
        verify(cartRepository, times(1)).findOrCreateByUserId(1L);
    }

    @Test
    void testAddToCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        when(cartRepository.findOrCreateByUserId(1L)).thenReturn(cart);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        CartDTO result = cartService.addToCart(1L, request);

        assertNotNull(result);
        verify(cartRepository, times(1)).findOrCreateByUserId(1L);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateCartItem() {
        cart.getItems().add(cartItem);

        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(3);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartDTO result = cartService.updateCartItem(1L, 1L, request);

        assertNotNull(result);
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testRemoveFromCart() {
        cart.getItems().add(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartDTO result = cartService.removeFromCart(1L, 1L);

        assertNotNull(result);
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
}