package com.ll.exam.sen_books.app.mybook.repository;

import com.ll.exam.sen_books.app.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    void deleteByProductIdAndOwnerId(Long id, Long id1);

    Optional<MyBook> findByProductIdAndOwnerId(Long productId, Long ownerId);
}