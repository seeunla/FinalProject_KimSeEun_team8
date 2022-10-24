package com.ll.exam.sen_books.app.base.initData;

import com.ll.exam.sen_books.app.cart.service.CartService;
import com.ll.exam.sen_books.app.member.service.MemberService;
import com.ll.exam.sen_books.app.post.service.PostService;
import com.ll.exam.sen_books.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestInitData implements InitDataBefore{
    @Bean
    CommandLineRunner initData(MemberService memberService, PostService postService, ProductService productService, CartService cartService) {
        return args -> {
            before(memberService, postService, productService, cartService);
        };
    }
}
