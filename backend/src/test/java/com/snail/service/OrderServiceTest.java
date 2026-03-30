package com.snail.service;

import com.snail.dto.CreateOrderRequest;
import com.snail.dto.OrderDTO;
import com.snail.entity.Order;
import com.snail.entity.Product;
import com.snail.repository.OrderRepository;
import com.snail.repository.ProductRepository;
import com.snail.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setOrderNumber("SNAIL1234567890");
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("99.99"));
        order.setShippingAddress("测试地址");
        order.setPhoneNumber("13800138000");
        order.setReceiverName("测试用户");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        product = new Product();
        product.setId(1L);
        product.setName("测试商品");
        product.setPrice(new BigDecimal("99.99"));
        product.setStock(10);
    }

    @Test
    void testCreateOrder() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setShippingAddress("测试地址");
        request.setPhoneNumber("13800138000");
        request.setReceiverName("测试用户");

        CreateOrderRequest.OrderProductItem item = new CreateOrderRequest.OrderProductItem();
        item.setProductId(1L);
        item.setQuantity(1);
        request.setProductItems(Arrays.asList(item));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO result = orderService.createOrder(1L, request);

        assertNotNull(result);
        assertEquals("SNAIL1234567890", result.getOrderNumber());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testGetUserOrders() {
        List<Order> orderList = Arrays.asList(order);
        Page<Order> orderPage = new PageImpl<>(orderList, PageRequest.of(0, 10), 1);
        when(orderRepository.findByUserId(1L, any(PageRequest.class))).thenReturn(orderPage);

        Page<OrderDTO> result = orderService.getUserOrders(1L, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(orderRepository, times(1)).findByUserId(1L, any(PageRequest.class));
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testCancelOrder() {
        order.setStatus(Order.OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO result = orderService.cancelOrder(1L, 1L);

        assertNotNull(result);
        assertEquals(Order.OrderStatus.CANCELLED, result.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}