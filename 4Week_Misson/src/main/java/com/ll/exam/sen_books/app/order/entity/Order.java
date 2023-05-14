package com.ll.exam.sen_books.app.order.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Table(name = "product_order")
public class Order extends BaseEntity {
    private LocalDateTime refundDate;
    private LocalDateTime payDate;
    private LocalDateTime cancelDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member buyer;
    private String name;
    private int payPrice;                   // 총 주문 결제 금액
    private int pgPayPrice;                 // 총 pg 결제 금액
    private int cashPayPrice;               // 총 캐시 결제 금액
    private boolean readyStatus; // 주문완료여부
    private boolean isPaid; // 결제여부
    private boolean isCanceled; // 취소여부
    private boolean isRefunded; // 환불여부

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(long id) {
        super(id);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);

        orderItems.add(orderItem);
    }

    public void setOrderDone() {
        this.readyStatus = true;
    }

    // 총 주문(상품) 금액
    public int calculatePayPrice() {
        int payPrice = 0;
        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getPayPrice();
        }
        return payPrice;
    }

    public void setCancelDone() {
        cancelDate = LocalDateTime.now();
        readyStatus = false;
        isCanceled = true;
    }

    // 캐시 전액 결제 완료 처리
    public void setPaymentDone(int payPrice) {
        this.payDate = LocalDateTime.now();
        // 총 결제 금액 == 캐시 결제 금액
        this.payPrice = payPrice;
        this.cashPayPrice = payPrice;
        // 주문 품목 결제 완료 처리
        for(OrderItem orderItem : orderItems) {
            orderItem.setPaymentDone();
        }
        this.isPaid = true;
    }

    // TossPayments 결제 완료 처리
    public void setPaymentDone(int payPrice, int pgPayPrice) {
        for (OrderItem orderItem : orderItems) {
            orderItem.setPaymentDone();
        }
        this.isPaid = true;
        // 총 결제 금액 == pg 결제 금액 + 캐시 결제 금액
        this.payPrice = payPrice;
        this.pgPayPrice = pgPayPrice;
        this.cashPayPrice = payPrice - pgPayPrice;
        payDate = LocalDateTime.now();
    }

    public void setRefundDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setRefundDone();
        }

        isRefunded = true;
        this.refundDate = LocalDateTime.now();
    }

    public void makeName() {
        String name = orderItems.get(0).getProduct().getSubject();

        if ( orderItems.size() > 1 ) {
            name += " 외 %d개".formatted(orderItems.size() - 1);
        }

        this.name = name;
    }
    public boolean isPayable() {
        if (isPaid) return  false;
        if (isCanceled) return false;
        if (!readyStatus) return false;

        return true;
    }

    public void setCanceled() {
        isCanceled = true;
    }

    public boolean isCancellable() {
        if (!readyStatus) return false;
        if(isPaid) return false;

        return true;
    }

    // 주문 완료 상태
    public boolean isOrderedStatus() {
        if(!readyStatus) return false;
        if(isPaid) return false;
        if(isCanceled) return false;
        if(isRefunded) return false;
        return true;
    }

    // 주문 취소 완료 상태
    public boolean isCanceledStatus() {
        if(readyStatus) return false;
        if(isPaid) return false;
        if(!isCanceled) return false;
        if(isRefunded) return false;
        return true;
    }

    // 결제 완료 상태
    public boolean isPaidStatus() {
        if(!readyStatus) return false;
        if(!isPaid) return false;
        if(isCanceled) return false;
        if(isRefunded) return false;
        return true;
    }

    // 환불 완료 상태
    public boolean isRefundedStatus() {
        if(!readyStatus) return false;
        if(!isPaid) return false;
        if(isCanceled) return false;
        if(!isRefunded) return false;
        return true;
    }

    // 환불 가능 여부
    public boolean isRefundable() {
        if(!isPaidStatus()) return false;
        if(isAfterRefundDeadline()) return false;
        return true;
    }

    public boolean isAfterRefundDeadline() {
        if(payDate != null) {
            // 현재 일시가 결제 일시보다 10분 이후이면
            LocalDateTime refundDeadline = payDate.plusMinutes(10); // 환불 마감 기한
            if(LocalDateTime.now().isAfter(refundDeadline)) return true;
        }
        return false;
    }
}
