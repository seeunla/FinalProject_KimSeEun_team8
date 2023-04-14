package com.ll.exam.sen_books.service;

import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.member.service.MemberService;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.post.service.PostService;
import com.ll.exam.sen_books.app.product.entity.Product;
import com.ll.exam.sen_books.app.product.form.ProductForm;
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
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("상품 등록")
    void t1() {
        Member author = memberService.findById(1).orElse(null);
        ProductForm productForm = new ProductForm();
        productForm.setPostId(1);
        productForm.setProductKeywords("하나");
        productForm.setSubject("제목");
        productForm.setPrice(1000);
        productForm.setContent("안녕");

        Product product = productService.create(author, productForm);

        assertThat(product).isNotNull();
        assertThat(product.getSubject()).isEqualTo("테스트내용1");
        assertThat(product.getPrice()).isEqualTo(1_900);
    }
}
