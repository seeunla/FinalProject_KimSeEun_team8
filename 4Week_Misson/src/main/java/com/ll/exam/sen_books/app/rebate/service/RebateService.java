package com.ll.exam.sen_books.app.rebate.service;

import com.ll.exam.sen_books.app.cash.entity.CashLog;
import com.ll.exam.sen_books.app.member.service.MemberService;
import com.ll.exam.sen_books.app.order.entity.OrderItem;
import com.ll.exam.sen_books.app.order.service.OrderService;
import com.ll.exam.sen_books.app.rebate.entity.RebateOrderItem;
import com.ll.exam.sen_books.app.rebate.repository.RebateOrderItemRepository;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RebateService {
    private final OrderService orderService;
    private final MemberService memberService;
    private final RebateOrderItemRepository rebateOrderItemRepository;

    @Transactional
    public void makeDate(int year, int month) {
        int monthEndDay = Ut.date.getEndDay(year, month);

        // 1. 정산 데이터를 생성할 날짜 범위 구하기
        LocalDateTime fromDate = Ut.date.getStartOfDay(year, month, 1);
        LocalDateTime toDate = Ut.date.getEndOfDay(year, month, monthEndDay);

        // 2. 해당 범위의 모든 주문 품목 조회
        List<OrderItem> orderItems = orderService.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);

        // 3. 주문 데이터 -> 정산 데이터 변환
        List<RebateOrderItem> rebateOrderItems = orderItems
                .stream()
                .map(this::toRebateOrderItem)
                .collect(Collectors.toList());

        // 4. 정산 데이터 생성
        rebateOrderItems.forEach(this::makeRebateOrderItem);
    }

    @Transactional
    public void makeRebateOrderItem(RebateOrderItem item) {
        RebateOrderItem oldRebateOrderItem = rebateOrderItemRepository.findByOrderItemId(item.getOrderItem().getId()).orElse(null);

        if (oldRebateOrderItem != null) {
            rebateOrderItemRepository.delete(oldRebateOrderItem);
        }

        rebateOrderItemRepository.save(item);
    }

    // OrderItem -> RebateOrderItem 변환
    public RebateOrderItem toRebateOrderItem(OrderItem orderItem) {
        return new RebateOrderItem(orderItem);
    }

    public List<RebateOrderItem> findRebateOrderItemsByPayDateIn(String yearMonth) {
        int monthEndDay = Ut.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.date.parse(fromDateStr);
        LocalDateTime toDate = Ut.date.parse(toDateStr);

        return rebateOrderItemRepository.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);
    }

    @Transactional
    public void rebate(long orderItemId) {
        RebateOrderItem rebateOrderItem = rebateOrderItemRepository.findByOrderItemId(orderItemId).get();

        if (rebateOrderItem.isRebateAvailable() == false) {
            return;
        }

        int calculateRebatePrice = rebateOrderItem.calculateRebatePrice();

        MemberService.AddCashDataBody addCashRsData = memberService.addCash(rebateOrderItem.getProduct().getAuthor(), calculateRebatePrice, "정산__%d__지급__예치금".formatted(rebateOrderItem.getOrderItem().getId()));
        CashLog cashLog = addCashRsData.getCashLog();

        rebateOrderItem.setRebateDone(cashLog.getId());
    }

    public RebateOrderItem findByOrderItemId(long orderItemId) {
        return rebateOrderItemRepository.findByOrderItemId(orderItemId).get();
    }
}
