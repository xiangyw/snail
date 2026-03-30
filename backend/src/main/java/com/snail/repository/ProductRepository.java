package com.snail.repository;

import com.snail.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品数据访问接口
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * 根据商品名称模糊搜索
     */
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.isActive = true")
    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 根据分类ID查询商品
     */
    Page<Product> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);
    
    /**
     * 查询价格区间内的商品
     */
    Page<Product> findByPriceBetweenAndIsActiveTrue(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    /**
     * 查询热门商品（库存充足且活跃）
     */
    @Query("SELECT p FROM Product p WHERE p.stock > 0 AND p.isActive = true ORDER BY p.createdAt DESC")
    List<Product> findTopProducts(Pageable pageable);
    
    /**
     * 检查商品是否存在且库存充足
     */
    boolean existsByIdAndStockGreaterThanAndIsActiveTrue(Long id, int stock);
}