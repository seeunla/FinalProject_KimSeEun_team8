package com.ll.exam.sen_books.app.product.repository;

import com.ll.exam.sen_books.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
