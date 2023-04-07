package com.ll.exam.sen_books.app.keyword.repository;

import com.ll.exam.sen_books.app.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByContent(String content);
}
