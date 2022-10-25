package com.ll.exam.sen_books.app.product.service;

import com.ll.exam.sen_books.app.hashTag.entity.HashTag;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.product.entity.Product;
import com.ll.exam.sen_books.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Product create(Post post, String subject, int price) {
        Product product = Product.builder()
                .subject(subject)
                .post(post)
                .author(post.getAuthor())
                .price(price)
                .build();

        productRepository.save(product);

        return product;
    }

    @Transactional
    public void modify(Product product, String subject, int price) {
        product.modify(subject, price);
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    public List<Product> findAllByAuthorId(Long id) {
        return productRepository.findAllByAuthorId(id);
    }

    public Optional<Product> findForPrintById(long id) {
        Optional<Product> product = findById(id);

        if (product.isEmpty()) {
            throw new NoSuchElementException();
        }

        return product;
    }
}
