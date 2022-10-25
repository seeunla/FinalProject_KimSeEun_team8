package com.ll.exam.sen_books.app.order.repository;

import com.ll.exam.sen_books.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
