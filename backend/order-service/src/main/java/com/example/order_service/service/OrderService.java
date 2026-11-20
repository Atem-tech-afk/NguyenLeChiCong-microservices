package com.example.order_service.service;

import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> getById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> getByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    @Transactional
    public Order create(Order order) {
        order.setId(null);
        BigDecimal total = BigDecimal.ZERO;

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setId(null);
                item.setOrder(order);
                if (item.getUnitPrice() != null && item.getQuantity() != null) {
                    BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    item.setSubtotal(subtotal);
                    total = total.add(subtotal);
                }
            }
        }
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    @Transactional
    public Optional<Order> updateStatus(Long id, String status) {
        return orderRepository.findById(id).map(existing -> {
            existing.setStatus(status);
            return orderRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
