package com.snail.service;

import com.snail.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * 商品服务接口
 */
public interface ProductService {
    
    /**
     * 获取商品分页列表
     */
    Page<ProductDTO> getProducts(Pageable pageable);
    
    /**
     * 根据ID获取商品详情
     */
    ProductDTO getProductById(Long id);
    
    /**
     * 根据关键词搜索商品
     */
    Page<ProductDTO> searchProducts(String keyword, Pageable pageable);
    
    /**
     * 根据分类ID获取商品
     */
    Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable);
    
    /**
     * 获取价格区间内的商品
     */
    Page<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    /**
     * 创建商品
     */
    ProductDTO createProduct(ProductDTO productDTO);
    
    /**
     * 更新商品
     */
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    
    /**
     * 删除商品
     */
    void deleteProduct(Long id);
    
    /**
     * 检查商品库存是否充足
     */
    boolean checkStock(Long productId, Integer quantity);
}