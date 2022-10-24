package com.ll.exam.sen_books.app.product.service;

import com.ll.exam.sen_books.app.product.entity.Product;
import com.ll.exam.sen_books.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }
}
