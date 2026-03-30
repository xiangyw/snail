package com.snail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商品信息")
public class ProductDTO {
    
    @Schema(description = "商品ID")
    private Long id;
    
    @NotBlank(message = "商品名称不能为空")
    @Schema(description = "商品名称", required = true)
    private String name;
    
    @Schema(description = "商品描述")
    private String description;
    
    @NotNull(message = "商品价格不能为空")
    @Positive(message = "商品价格必须大于0")
    @Schema(description = "商品价格", required = true)
    private BigDecimal price;
    
    @NotNull(message = "商品库存不能为空")
    @Min(value = 0, message = "商品库存不能小于0")
    @Schema(description = "商品库存", required = true)
    private Integer stock;
    
    @Schema(description = "商品图片URL")
    private String imageUrl;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "是否激活")
    private Boolean isActive = true;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    /**
     * 构造函数，从商品实体创建DTO
     */
    public static ProductDTO fromEntity(com.snail.entity.Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImageUrl(product.getImageUrl());
        dto.setCategoryId(product.getCategoryId());
        dto.setIsActive(product.getIsActive());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}