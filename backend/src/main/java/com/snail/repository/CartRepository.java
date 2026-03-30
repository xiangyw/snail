package com.snail.repository;

import com.snail.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 购物车数据访问接口
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    /**
     * 根据用户ID查找购物车
     */
    Optional<Cart> findByUserId(Long userId);
    
    /**
     * 根据用户ID查找或创建购物车
     */
    default Cart findOrCreateByUserId(Long userId) {
        return findByUserId(userId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUserId(userId);
            return save(cart);
        });
    }
}