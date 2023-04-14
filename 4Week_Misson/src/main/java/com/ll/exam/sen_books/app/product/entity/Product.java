package com.ll.exam.sen_books.app.product.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.postKeyword.entity.PostKeyword;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access=PROTECTED)
public class Product extends BaseEntity {
    private String subject;
    @ManyToOne
    private Member author; // 작가
    @ManyToOne(fetch = LAZY)
    private PostKeyword postKeyword; // 게시글 키워드
    @Column(columnDefinition = "TEXT")
    private String content; // 상품설명
    private int price; // 판매가

    public Product(long id) {
        super(id);
    }

    public int getSalePrice() {
        return getPrice();
    }

    public int getWholesalePrice() {
        return (int) Math.ceil(getPrice() * 0.7);
    }

    public String getJdenticon() {
        return "product__" + getId();
    }

    public void modify(String subject, int price) {
        this.subject = subject;
        this.price = price;
    }

    public boolean isOrderable() {
        return true;
    }
}
