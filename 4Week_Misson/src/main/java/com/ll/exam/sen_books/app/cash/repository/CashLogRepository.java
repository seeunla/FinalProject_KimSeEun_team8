package com.ll.exam.sen_books.app.cash.repository;

import com.ll.exam.sen_books.app.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
