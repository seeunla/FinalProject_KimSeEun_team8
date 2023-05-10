package com.ll.exam.sen_books.app.cash.service;

import com.ll.exam.sen_books.app.cash.entity.CashLog;
import com.ll.exam.sen_books.app.cash.repository.CashLogRepository;
import com.ll.exam.sen_books.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashService {
    private final CashLogRepository cashLogRepository;

    public CashLog addCash(Member member, int price, String eventType) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        return cashLog;
    }
}
