package com.ll.exam.sen_books.app.base.initData;

import com.ll.exam.sen_books.app.cart.service.CartService;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.member.service.MemberService;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.post.form.PostForm;
import com.ll.exam.sen_books.app.post.service.PostService;
import com.ll.exam.sen_books.app.product.entity.Product;
import com.ll.exam.sen_books.app.product.service.ProductService;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService, ProductService productService, CartService cartService) {
        Member member1 = memberService.join("user1", "1234", "user1@test.com", "하니");
        Member member2 = memberService.join("user2", "1234", "user2@test.com", "원영이");

        PostForm postForm = new PostForm("글1", "내용2","음악", "음악");
        postForm.setSubject("글1");
        postForm.setContent("내용2");

        Post post1 =  postService.write(member1, postForm);
        Post post2 =  postService.write(member1, postForm);
        Post post3 =  postService.write(member2, postForm);
        Post post4 =  postService.write(member2, postForm);

        Product product1 = productService.create(post1, "음악", 12_000);
        Product product2 = productService.create(post2, "미술", 15_000);
        Product product3 = productService.create(post3, "사진", 21_000);
        Product product4 = productService.create(post4, "역사", 20_000);

        cartService.addItem(member1, product1);
        cartService.addItem(member1, product2);

        cartService.addItem(member2, product3);
        cartService.addItem(member2, product4);
    }
}
