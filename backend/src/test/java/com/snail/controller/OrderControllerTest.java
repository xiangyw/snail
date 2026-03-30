package com.snail.controller;

import com.snail.dto.CreateOrderRequest;
import com.snail.dto.OrderDTO;
import com.snail.entity.Order;
import com.snail.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void testCreateOrder() throws Exception {
        OrderDTO order = new OrderDTO();
        order.setId(1L);
        order.setOrderNumber("SNAIL1234567890");
        order.setTotalAmount(new BigDecimal("99.99"));

        CreateOrderRequest request = new CreateOrderRequest();
        request.setShippingAddress("测试地址");
        request.setPhoneNumber("13800138000");
        request.setReceiverName("测试用户");

        when(orderService.createOrder(1L, any(CreateOrderRequest.class))).thenReturn(order);

        String requestJson = "{ \"shippingAddress\": \"测试地址\", \"phoneNumber\": \"13800138000\", \"receiverName\": \"测试用户\" }";

        mockMvc.perform(post("/api/orders?userId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNumber").value("SNAIL1234567890"));
    }

    @Test
    void testGetUserOrders() throws Exception {
        OrderDTO order = new OrderDTO();
        order.setId(1L);
        order.setOrderNumber("SNAIL1234567890");

        Page<OrderDTO> orderPage = new PageImpl<>(Collections.singletonList(order));

        when(orderService.getUserOrders(1L, PageRequest.of(0, 10))).thenReturn(orderPage);

        mockMvc.perform(get("/api/orders?userId=1&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void testGetOrderById() throws Exception {
        OrderDTO order = new OrderDTO();
        order.setId(1L);
        order.setOrderNumber("SNAIL1234567890");

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCancelOrder() throws Exception {
        OrderDTO order = new OrderDTO();
        order.setId(1L);
        order.setStatus(Order.OrderStatus.CANCELLED);

        when(orderService.cancelOrder(1L, 1L)).thenReturn(order);

        mockMvc.perform(put("/api/orders/1/cancel?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}