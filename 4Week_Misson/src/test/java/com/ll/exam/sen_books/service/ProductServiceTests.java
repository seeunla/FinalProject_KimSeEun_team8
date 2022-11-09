package com.ll.exam.sen_books.service;

import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.post.service.PostService;
import com.ll.exam.sen_books.app.product.entity.Product;
import com.ll.exam.sen_books.app.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ProductServiceTests {
    @Autowired
    private PostService postService;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 등록")
    void t1() {
        Post post = postService.findById(2).get();

        Product product = productService.create(post, "테스트내용1", 1_900);

        assertThat(product).isNotNull();
        assertThat(product.getSubject()).isEqualTo("테스트내용1");
        assertThat(product.getPrice()).isEqualTo(1_900);
    }
}
