package com.ll.exam.sen_books.app.order.repository;

import com.ll.exam.sen_books.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByBuyerId(Long id);
}
