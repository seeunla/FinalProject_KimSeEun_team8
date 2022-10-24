package com.ll.exam.sen_books.app.cart.repository;


import com.ll.exam.sen_books.app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByBuyerIdAndProductId(Long id);


    List<CartItem> findAllByBuyerId(long buyerId);
}
