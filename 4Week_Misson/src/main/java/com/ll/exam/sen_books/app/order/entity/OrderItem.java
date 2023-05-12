package com.ll.exam.sen_books.app.order.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Order order;
    @ManyToOne(fetch = LAZY)
    private Product product;

    // 가격
    private LocalDateTime payDate;      // 결제 일시
    private LocalDateTime refundDate;   // 환불 일시
    private int price; // 권장판매가
    private int payPrice; // 실제판매가
    private int wholesalePrice; // 도매가
    private int pgFee; // 결제대행사 수수료
    private int salePrice; // 결제금액
    private int refundPrice; // 환불금액
    private boolean isPaid; // 결제여부
    private boolean isRefund;       // 환불 여부
    private int quantity;

    public OrderItem(Product product) {
        this.product = product;
        this.price = product.getPrice();
        this.payPrice = product.getSalePrice();
        this.wholesalePrice = product.getWholesalePrice();
    }

    public int calculatePayPrice() {
        return salePrice * quantity;
    }

    public void setPaymentDone() {
        this.pgFee = 0;
        this.payPrice = salePrice;
        this.isPaid = true;
        this.payDate = LocalDateTime.now();
    }

    public void setRefundDone() {
        this.refundPrice = payPrice;
        this.isRefund = true;
        this.refundDate = LocalDateTime.now();
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
