package com.example.RestaurantProjectV2.controller;

import com.example.RestaurantProjectV2.domain.OrderEntity;
import com.example.RestaurantProjectV2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public List<OrderEntity> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public OrderEntity getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderEntity> addOrder(@Validated @RequestBody OrderEntity order) {
        HttpStatus status = HttpStatus.CREATED;
        OrderEntity saved = orderService.save(order);
        return new ResponseEntity<>(saved, status);
    }

    @PutMapping("/orders/{id}")
    public OrderEntity updateOrder(@PathVariable Long id, @RequestBody OrderEntity updatedOrderEntity) {
        OrderEntity existingOrderEntity = orderService.getOrderById(id);

        if (existingOrderEntity != null) {
            existingOrderEntity.setName(updatedOrderEntity.getName());
            existingOrderEntity.setQuantity(updatedOrderEntity.getQuantity());
            return orderService.save(existingOrderEntity);
        } else {
            return null;
        }
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
    }
}