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

    public int calculatePayPrice() {
        int payPrice = 0;

        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getPayPrice();
        }

        return payPrice;
    }

    public void setCancelDone() {
        cancelDate = LocalDateTime.now();

        isCanceled = true;
    }

    public void setPaymentDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setPaymentDone();
        }
    }

    public void setRefundDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setRefundDone();
        }

        isRefunded = true;
    }

    public long getTotalPayPrice() {
        long payPrice = 0;
        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getSalePrice();
        }

        return payPrice;
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

        return true;
    }


    public void setCanceled() {
        isCanceled = true;
    }
}
