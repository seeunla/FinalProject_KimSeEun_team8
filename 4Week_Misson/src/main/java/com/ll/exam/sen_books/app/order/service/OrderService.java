package com.ll.exam.sen_books.app.order.service;

import com.ll.exam.sen_books.app.cart.entity.CartItem;
import com.ll.exam.sen_books.app.cart.service.CartService;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.member.service.MemberService;
import com.ll.exam.sen_books.app.order.entity.Order;
import com.ll.exam.sen_books.app.order.entity.OrderItem;
import com.ll.exam.sen_books.app.order.repository.OrderItemRepository;
import com.ll.exam.sen_books.app.order.repository.OrderRepository;
import com.ll.exam.sen_books.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final MemberService memberService;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(Member buyer, String ids) {
        // 입력된 회원의 장바구니 아이템들을 전부 가져온다.

        // 만약에 특정 장바구니의 상품옵션이 판매불능이면 삭제
        // 만약에 특정 장바구니의 상품옵션이 판매가능이면 주문품목으로 옮긴 후 삭제

        String[] idsArr = ids.split(",");
        // Array -> List
        List<Long> cartItemIds = Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(Collectors.toList());

        List<CartItem> cartItems = cartService.findByMemberIdOrderByIdDesc(buyer, cartItemIds);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.isOrderable()) {
                orderItems.add(new OrderItem(product));
            }

            cartService.removeItem(buyer, product);
        }

        return create(buyer, orderItems);
    }

    @Transactional
    public Order create(Member buyer, List<OrderItem> orderItems) {
        Order order = Order
                .builder()
                .buyer(buyer)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        // 주문 품목으로 부터 이름을 만든다.
        order.makeName();
        order.setOrderDone();
        orderRepository.save(order);

        return order;
    }

    public Optional<Order> findForPrintById(long id) {
        return findById(id);
    }

    public Optional<Order> findById(long id) {
        return orderRepository.findById(id);
    }

    public boolean memberCanSee(Member member, Order order) {
        return member.getId().equals(order.getBuyer().getId());
    }

    public List<Order> getOrders(Member buyer) {
        return orderRepository.findAllByBuyerId(buyer.getId());
    }


    @Transactional
    public void removeOrder(Order order) {
        orderRepository.delete(order);
    }

    @Transactional
    public void payByRestCashOnly(Order order) {
        Member orderer = order.getBuyer();

        long restCash = orderer.getRestCash();

        int payPrice = order.calculatePayPrice();

        if (payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }

        memberService.addCash(orderer, payPrice * -1, "주문결제__예치금결제");

        order.setPaymentDone();
        orderRepository.save(order);
    }

    @Transactional
    public void payByTossPayments(Order order, long useRestCash) {
        Member buyer = order.getBuyer();
        int payPrice = order.calculatePayPrice();

        long pgPayPrice = payPrice - useRestCash;
        memberService.addCash(buyer, pgPayPrice, "주문__%d__충전__토스페이먼츠".formatted(order.getId()));
        memberService.addCash(buyer, pgPayPrice * -1, "주문__%d__사용__토스페이먼츠".formatted(order.getId()));

        if ( useRestCash > 0 ) {
            memberService.addCash(buyer, useRestCash * -1, "주문__%d__사용__예치금".formatted(order.getId()));
        }

        order.setPaymentDone();
        orderRepository.save(order);
    }

    @Transactional
    public void cancel(Long orderId) {
        Order order = findById(orderId).get();
        order.setCanceled();
    }

    @Transactional
    public void refund(Long orderId) {
        Order order = findById(orderId).orElse(null);

        if (order == null) {
            return;
        }

        order.setCancelDone();

        long payPrice = order.getPayPrice();
        memberService.addCash(order.getBuyer(), payPrice, "주문_%d_환불_예치금".formatted(order.getId()));

        order.setRefundDone();
        orderRepository.save(order);
    }

    public List<OrderItem> findAllByPayDateBetweenOrderByIdAsc(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderItemRepository.findAllByPayDateBetween(fromDate, toDate);
    }

    public boolean canCancel(Member member, Order order) {
        if (!order.isCancellable()) return false;
        return memberCanSee(member, order);
    }
}
