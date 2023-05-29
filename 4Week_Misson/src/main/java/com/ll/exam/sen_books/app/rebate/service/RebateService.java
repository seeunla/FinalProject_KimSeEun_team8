package com.ll.exam.sen_books.app.rebate.service;

import com.ll.exam.sen_books.app.cash.entity.CashLog;
import com.ll.exam.sen_books.app.member.service.MemberService;
import com.ll.exam.sen_books.app.order.entity.OrderItem;
import com.ll.exam.sen_books.app.order.service.OrderService;
import com.ll.exam.sen_books.app.rebate.entity.RebateOrderItem;
import com.ll.exam.sen_books.app.rebate.exception.RebateOrderItemNotFoundException;
import com.ll.exam.sen_books.app.rebate.repository.RebateOrderItemRepository;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    // 정산 데이터 생성 가능한지 검증
    private boolean canMakeData(int year, int month) {
        LocalDateTime dataCreationDate = LocalDateTime.of(year, month, 15, 4, 0)
                .plusMonths(1);     // 정산 데이터 예상 생성일시
        // 현재 날짜 기준 해당 월의 정산 데이터를 생성가능한지 검증
        if(LocalDateTime.now().isBefore(dataCreationDate)) {
            throw new RuntimeException("%d-%2d 의 정산 데이터 생성은 %s 이후에 가능합니다.".formatted(
                    year, month,
                    dataCreationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))
            ));
        }
        return true;
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

    public List<RebateOrderItem> findRebateOrderItemsByPayDateIn(int year, int month) {
        int monthEndDay = Ut.date.getEndDay(year, month);


        LocalDateTime fromDate = Ut.date.getStartOfDay(year, month, 1); // 해당일자의 시작일시
        LocalDateTime toDate = Ut.date.getEndOfDay(year, month, monthEndDay); // 해당일자의 종료일시

        return rebateOrderItemRepository.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);
    }

    // 단건 정산 처리
    @Transactional
    public void rebate(long orderItemId) {
        RebateOrderItem rebateOrderItem = rebateOrderItemRepository.findByOrderItemId(orderItemId).get();
        // 1. 정산 가능 상태인지 검증
        if (!rebateOrderItem.isRebateAvailable()) {
            throw new RuntimeException("정ㅅ나 처리가 가능한 상태가 아닙니다.");
        }

        int calculateRebatePrice = rebateOrderItem.calculateRebatePrice();

        // 2. 판매자에게 예치금으로 정산금액 지급
        CashLog cashLog = memberService.addCash(
                rebateOrderItem.getSeller(),
                rebateOrderItem.calculateRebatePrice(),
                "정산금액지급__캐시__정산__%d".formatted(rebateOrderItem.getOrderItem().getId()));
        // 3. 정산 완료 처리
        rebateOrderItem.setRebateDone(cashLog);
    }

    public RebateOrderItem findById(long orderItemId) {
        return rebateOrderItemRepository.findByOrderItemId(orderItemId).orElseThrow(
                () -> {throw new RebateOrderItemNotFoundException("정산 데이터가 존재하지 않습니다.");}
        );
    }
}
