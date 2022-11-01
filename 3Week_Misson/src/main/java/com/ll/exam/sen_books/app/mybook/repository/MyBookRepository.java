package com.ll.exam.sen_books.app.mybook.repository;

import com.ll.exam.sen_books.app.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
}
