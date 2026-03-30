package com.snail.service.impl;

import com.snail.dto.CreateOrderRequest;
import com.snail.dto.OrderDTO;
import com.snail.entity.*;
import com.snail.repository.*;
import com.snail.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Override
    @Transactional
    public OrderDTO createOrder(Long userId, CreateOrderRequest request) {
        // 计算订单总金额和验证商品信息
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        if (request.getCartItemIds() != null && !request.getCartItemIds().isEmpty()) {
            // 从购物车创建订单
            orderItems = createOrderItemsFromCart(userId, request.getCartItemIds());
        } else if (request.getProductItems() != null && !request.getProductItems().isEmpty()) {
            // 直接购买创建订单
            orderItems = createOrderItemsFromProducts(request.getProductItems());
        } else {
            throw new IllegalArgumentException("订单项不能为空");
        }
        
        // 计算总金额
        for (OrderItem item : orderItems) {
            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        
        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(request.getShippingAddress());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setReceiverName(request.getReceiverName());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        
        // 关联订单项
        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
        }
        
        // 扣减库存
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
        
        // 如果是从购物车创建的订单，清空购物车项
        if (request.getCartItemIds() != null && !request.getCartItemIds().isEmpty()) {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("购物车不存在"));
            cart.getItems().removeIf(item -> request.getCartItemIds().contains(item.getId()));
            cartRepository.save(cart);
        }
        
        return convertToDTO(savedOrder, orderItems);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new NoSuchElementException("订单不存在，订单号: " + orderNumber);
        }
        return convertToDTO(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("订单不存在，ID: " + orderId));
        return convertToDTO(order);
    }
    
    @Override
    @Transactional
    public OrderDTO cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("订单不存在，ID: " + orderId));
        
        if (!order.getUserId().equals(userId)) {
            throw new SecurityException("无权操作他人订单");
        }
        
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new IllegalStateException("只能取消待支付的订单");
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order cancelledOrder = orderRepository.save(order);
        return convertToDTO(cancelledOrder);
    }
    
    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("订单不存在，ID: " + orderId));
        
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    /**
     * 从购物车创建订单项
     */
    private List<OrderItem> createOrderItemsFromCart(Long userId, List<Long> cartItemIds) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("购物车不存在"));
        
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cart.getItems().stream()
                    .filter(item -> item.getId().equals(cartItemId))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("购物车项不存在，ID: " + cartItemId));
            
            // 检查库存是否充足
            if (cartItem.getProduct().getStock() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("商品库存不足: " + cartItem.getProduct().getName());
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setUpdatedAt(LocalDateTime.now());
            
            orderItems.add(orderItem);
        }
        
        return orderItems;
    }
    
    /**
     * 从商品列表创建订单项
     */
    private List<OrderItem> createOrderItemsFromProducts(List<CreateOrderRequest.OrderProductItem> productItems) {
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (CreateOrderRequest.OrderProductItem item : productItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("商品不存在，ID: " + item.getProductId()));
            
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("商品库存不足: " + product.getName());
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setUpdatedAt(LocalDateTime.now());
            
            orderItems.add(orderItem);
        }
        
        return orderItems;
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNumber() {
        return "SNAIL" + System.currentTimeMillis() + String.format("%06d", new Random().nextInt(1000000));
    }
    
    /**
     * 将订单实体转换为DTO（带订单项）
     */
    private OrderDTO convertToDTO(Order order, List<OrderItem> orderItems) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setReceiverName(order.getReceiverName());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        // 转换订单项
        List<OrderDTO.OrderItemDTO> itemDTOs = orderItems.stream().map(item -> {
            OrderDTO.OrderItemDTO itemDTO = new OrderDTO.OrderItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProduct(com.snail.dto.ProductDTO.fromEntity(item.getProduct()));
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setSubtotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            itemDTO.setCreatedAt(item.getCreatedAt());
            itemDTO.setUpdatedAt(item.getUpdatedAt());
            return itemDTO;
        }).collect(Collectors.toList());
        
        dto.setItems(itemDTOs);
        
        return dto;
    }
    
    /**
     * 将订单实体转换为DTO（不带订单项，需要单独查询）
     */
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setReceiverName(order.getReceiverName());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        // 查询并设置订单项
        List<OrderItem> orderItems = order.getItems();
        if (orderItems != null && !orderItems.isEmpty()) {
            List<OrderDTO.OrderItemDTO> itemDTOs = orderItems.stream().map(item -> {
                OrderDTO.OrderItemDTO itemDTO = new OrderDTO.OrderItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setProduct(com.snail.dto.ProductDTO.fromEntity(item.getProduct()));
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setSubtotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
                itemDTO.setCreatedAt(item.getCreatedAt());
                itemDTO.setUpdatedAt(item.getUpdatedAt());
                return itemDTO;
            }).collect(Collectors.toList());
            
            dto.setItems(itemDTOs);
        }
        
        return dto;
    }
}