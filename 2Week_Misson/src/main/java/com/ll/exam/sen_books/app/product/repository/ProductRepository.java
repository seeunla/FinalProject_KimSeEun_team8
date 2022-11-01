package com.ll.exam.sen_books.app.product.repository;

import com.ll.exam.sen_books.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByAuthorId(Long id);
    Optional<Product> findById(Long id);
}
