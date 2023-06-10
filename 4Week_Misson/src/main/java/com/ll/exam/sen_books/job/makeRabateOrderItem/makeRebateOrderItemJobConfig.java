package com.ll.exam.sen_books.job.makeRabateOrderItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class makeRebateOrderItemJobConfig {
//    // 매 분 00초마다 실행
//    @Scheduled(cron = "0 * * * * *")
//    public void printJob() {
//        System.out.println(LocalDateTime.now());
//    }


}
