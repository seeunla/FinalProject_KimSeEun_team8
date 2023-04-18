package com.ll.exam.sen_books.app.product.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.hashTag.entity.ProductHashTag;
import com.ll.exam.sen_books.app.postKeyword.entity.PostKeyword;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access=PROTECTED)
@ToString(callSuper = true)
public class Product extends BaseEntity {
    private String subject;
    @ManyToOne
    private Member author; // 작가
    @ManyToOne(fetch = LAZY)
    private PostKeyword postKeyword; // 게시글 키워드
    @Column(columnDefinition = "TEXT")
    private String content; // 상품설명
    private int price; // 판매가

    @OneToMany(mappedBy = "product", cascade = {CascadeType.ALL})
    private List<ProductHashTag> productHashTags = new ArrayList<>();   // 도서 해시태그 리스트

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
