package com.ll.exam.sen_books.app.order.repository;

import com.ll.exam.sen_books.app.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
