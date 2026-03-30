package com.snail.controller;

import com.snail.dto.AddToCartRequest;
import com.snail.dto.CartDTO;
import com.snail.dto.UpdateCartItemRequest;
import com.snail.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/api/cart")
@Tag(name = "购物车管理", description = "购物车相关操作接口")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping
    @Operation(summary = "获取购物车", description = "获取当前用户的购物车信息")
    public ResponseEntity<CartDTO> getUserCart(@RequestParam Long userId) {
        CartDTO cart = cartService.getUserCart(userId);
        return ResponseEntity.ok(cart);
    }
    
    @PostMapping("/add")
    @Operation(summary = "添加到购物车", description = "将商品添加到购物车")
    public ResponseEntity<CartDTO> addToCart(@RequestParam Long userId, @Valid @RequestBody AddToCartRequest request) {
        CartDTO cart = cartService.addToCart(userId, request);
        return ResponseEntity.ok(cart);
    }
    
    @PutMapping("/item/{cartItemId}")
    @Operation(summary = "更新购物车项", description = "更新购物车中某项商品的数量")
    public ResponseEntity<CartDTO> updateCartItem(@RequestParam Long userId, @PathVariable Long cartItemId, 
                                                  @Valid @RequestBody UpdateCartItemRequest request) {
        CartDTO cart = cartService.updateCartItem(userId, cartItemId, request);
        return ResponseEntity.ok(cart);
    }
    
    @DeleteMapping("/item/{cartItemId}")
    @Operation(summary = "从购物车移除商品", description = "从购物车中移除指定商品")
    public ResponseEntity<CartDTO> removeFromCart(@RequestParam Long userId, @PathVariable Long cartItemId) {
        CartDTO cart = cartService.removeFromCart(userId, cartItemId);
        return ResponseEntity.ok(cart);
    }
    
    @DeleteMapping("/clear")
    @Operation(summary = "清空购物车", description = "清空当前用户的购物车")
    public ResponseEntity<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/count")
    @Operation(summary = "获取购物车商品数量", description = "获取当前用户购物车中商品的数量")
    public ResponseEntity<Integer> getCartItemCount(@RequestParam Long userId) {
        Integer count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(count);
    }
}