package com.ll.exam.sen_books.app.hashTag.repository;

import com.ll.exam.sen_books.app.hashTag.entity.ProductHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductHashTagRepository extends JpaRepository<ProductHashTag, Long> {
    List<ProductHashTag> findByProductId(Long productId);

    Optional<ProductHashTag> findByProductIdAndProductKeywordId(Long productId, Long productKeywordId);
}
