package com.snail.service;

import com.snail.dto.AddToCartRequest;
import com.snail.dto.CartDTO;
import com.snail.dto.UpdateCartItemRequest;

/**
 * 购物车服务接口
 */
public interface CartService {
    
    /**
     * 获取用户购物车
     */
    CartDTO getUserCart(Long userId);
    
    /**
     * 添加商品到购物车
     */
    CartDTO addToCart(Long userId, AddToCartRequest request);
    
    /**
     * 更新购物车项数量
     */
    CartDTO updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest request);
    
    /**
     * 从购物车移除商品
     */
    CartDTO removeFromCart(Long userId, Long cartItemId);
    
    /**
     * 清空购物车
     */
    void clearCart(Long userId);
    
    /**
     * 获取购物车商品总数
     */
    Integer getCartItemCount(Long userId);
}