package com.ll.exam.sen_books.app.product.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
    private Member author;
    @ManyToOne(fetch = LAZY)
    private Post post;
    private int price;

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
