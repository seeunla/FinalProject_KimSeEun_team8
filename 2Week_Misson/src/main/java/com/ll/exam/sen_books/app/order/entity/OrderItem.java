package com.ll.exam.sen_books.app.order.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Order order;

    private LocalDateTime payDate;

    @ManyToOne(fetch = LAZY)
    private Product product;

    // 가격
    private int price; // 권장판매가
    private int PayPrice; // 실제판매가
    private int wholesalePrice; // 도매가
    private int pgFee; // 결제대행사 수수료
    private int SalePrice; // 결제금액
    private int refundPrice; // 환불금액
    private boolean isPaid; // 결제여부
    private int quantity;

    public OrderItem(Product product) {
        this.product = product;
        this.price = product.getPrice();
        this.PayPrice = product.getSalePrice();
        this.wholesalePrice = product.getWholesalePrice();
    }

    public int calculatePayPrice() {
        return SalePrice * quantity;
    }

    public void setPaymentDone() {
        this.pgFee = 0;
        this.SalePrice = getPayPrice();
        this.isPaid = true;
        this.payDate = LocalDateTime.now();
    }

    public void setRefundDone() {
        this.refundPrice = SalePrice;
    }

}
