package com.ll.exam.sen_books.app.product.repository;

import com.ll.exam.sen_books.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByAuthorId(Long id);
}
