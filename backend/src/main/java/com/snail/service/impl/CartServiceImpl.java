package com.snail.service.impl;

import com.snail.dto.AddToCartRequest;
import com.snail.dto.CartDTO;
import com.snail.dto.UpdateCartItemRequest;
import com.snail.entity.Cart;
import com.snail.entity.CartItem;
import com.snail.entity.Product;
import com.snail.repository.CartRepository;
import com.snail.repository.ProductRepository;
import com.snail.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * 购物车服务实现类
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    @Transactional(readOnly = true)
    public CartDTO getUserCart(Long userId) {
        Cart cart = cartRepository.findOrCreateByUserId(userId);
        return convertToDTO(cart);
    }
    
    @Override
    public CartDTO addToCart(Long userId, AddToCartRequest request) {
        // 检查商品是否存在且库存充足
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("商品不存在，ID: " + request.getProductId()));
        
        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("商品库存不足");
        }
        
        Cart cart = cartRepository.findOrCreateByUserId(userId);
        
        // 检查购物车中是否已有该商品
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElse(null);
        
        if (existingItem != null) {
            // 如果已有该商品，更新数量
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new IllegalArgumentException("商品库存不足，当前库存：" + product.getStock());
            }
            existingItem.setQuantity(newQuantity);
            existingItem.setPrice(product.getPrice());
            existingItem.setUpdatedAt(LocalDateTime.now());
        } else {
            // 如果没有该商品，创建新的购物车项
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            newItem.setPrice(product.getPrice());
            newItem.setCreatedAt(LocalDateTime.now());
            newItem.setUpdatedAt(LocalDateTime.now());
            cart.getItems().add(newItem);
        }
        
        Cart savedCart = cartRepository.save(cart);
        return convertToDTO(savedCart);
    }
    
    @Override
    public CartDTO updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("购物车不存在"));
        
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("购物车项不存在"));
        
        Product product = cartItem.getProduct();
        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("商品库存不足，当前库存：" + product.getStock());
        }
        
        cartItem.setQuantity(request.getQuantity());
        cartItem.setUpdatedAt(LocalDateTime.now());
        
        Cart savedCart = cartRepository.save(cart);
        return convertToDTO(savedCart);
    }
    
    @Override
    public CartDTO removeFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("购物车不存在"));
        
        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        
        Cart savedCart = cartRepository.save(cart);
        return convertToDTO(savedCart);
    }
    
    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("购物车不存在"));
        
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer getCartItemCount(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("购物车不存在"));
        
        return cart.getItems().size();
    }
    
    /**
     * 将购物车实体转换为DTO
     */
    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            List<CartDTO.CartItemDTO> cartItemDTOs = new ArrayList<>();
            int totalCount = 0;
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for (com.snail.entity.CartItem item : cart.getItems()) {
                CartDTO.CartItemDTO itemDTO = new CartDTO.CartItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setProduct(com.snail.dto.ProductDTO.fromEntity(item.getProduct()));
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                
                BigDecimal subtotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
                itemDTO.setSubtotal(subtotal);
                
                itemDTO.setCreatedAt(item.getCreatedAt());
                itemDTO.setUpdatedAt(item.getUpdatedAt());
                
                cartItemDTOs.add(itemDTO);
                
                totalCount += item.getQuantity();
                totalAmount = totalAmount.add(subtotal);
            }
            
            dto.setItems(cartItemDTOs);
            dto.setTotalCount(totalCount);
            dto.setTotalAmount(totalAmount);
        } else {
            dto.setItems(new ArrayList<>());
            dto.setTotalCount(0);
            dto.setTotalAmount(BigDecimal.ZERO);
        }
        
        return dto;
    }
}