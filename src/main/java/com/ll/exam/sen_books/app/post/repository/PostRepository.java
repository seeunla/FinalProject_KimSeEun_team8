package com.ll.exam.sen_books.app.post.repository;

import com.ll.exam.sen_books.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
