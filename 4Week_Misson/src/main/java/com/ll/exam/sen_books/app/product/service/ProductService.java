package com.ll.exam.sen_books.app.product.service;

import com.ll.exam.sen_books.app.hashTag.entity.ProductHashTag;
import com.ll.exam.sen_books.app.hashTag.service.ProductHashTagService;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.postKeyword.entity.PostKeyword;
import com.ll.exam.sen_books.app.postKeyword.service.PostKeywordService;
import com.ll.exam.sen_books.app.product.entity.Product;
import com.ll.exam.sen_books.app.product.form.ProductForm;
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
    private final PostKeywordService postKeywordService;
    private final ProductHashTagService productHashTagService;

    @Transactional
    public Product create(Member author, ProductForm productForm) {
        PostKeyword postKeyword = postKeywordService.findById(productForm.getPostKeywordId());

        Product product = Product.builder()
                .subject(productForm.getSubject())
                .postKeyword(postKeyword)
                .author(author)
                .price(productForm.getPrice())
                .content(productForm.getContent())
                .build();

        productRepository.save(product);

        // 도서 해시태그 적용
        String productKeywords = productForm.getProductKeywords();
        if(productKeywords != null) {
            productHashTagService.apply(product, productKeywords);
        }

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

    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }

    public List<Product> findAllByOrderByIdDesc() {
        return productRepository.findAllByOrderByIdDesc();
    }
}
