package com.snail.controller;

import com.snail.dto.AddToCartRequest;
import com.snail.dto.CartDTO;
import com.snail.dto.UpdateCartItemRequest;
import com.snail.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    void testGetUserCart() throws Exception {
        CartDTO cart = new CartDTO();
        cart.setId(1L);
        cart.setUserId(1L);

        when(cartService.getUserCart(1L)).thenReturn(cart);

        mockMvc.perform(get("/api/cart?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void testAddToCart() throws Exception {
        CartDTO cart = new CartDTO();
        cart.setId(1L);

        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        when(cartService.addToCart(1L, any(AddToCartRequest.class))).thenReturn(cart);

        String requestJson = "{ \"productId\": 1, \"quantity\": 2 }";

        mockMvc.perform(post("/api/cart/add?userId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateCartItem() throws Exception {
        CartDTO cart = new CartDTO();
        cart.setId(1L);

        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(3);

        when(cartService.updateCartItem(1L, 1L, any(UpdateCartItemRequest.class))).thenReturn(cart);

        String requestJson = "{ \"quantity\": 3 }";

        mockMvc.perform(put("/api/cart/item/1?userId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testRemoveFromCart() throws Exception {
        CartDTO cart = new CartDTO();
        cart.setId(1L);

        when(cartService.removeFromCart(1L, 1L)).thenReturn(cart);

        mockMvc.perform(delete("/api/cart/item/1?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}