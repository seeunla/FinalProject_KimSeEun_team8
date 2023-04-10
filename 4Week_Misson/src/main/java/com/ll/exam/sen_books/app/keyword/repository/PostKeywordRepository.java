package com.ll.exam.sen_books.app.keyword.repository;

import com.ll.exam.sen_books.app.keyword.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {
    Optional<PostKeyword> findByContent(String content);
}
