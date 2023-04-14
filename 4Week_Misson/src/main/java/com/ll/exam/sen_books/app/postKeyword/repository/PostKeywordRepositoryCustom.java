package com.ll.exam.sen_books.app.postKeyword.repository;

import com.ll.exam.sen_books.app.postKeyword.dto.PostKeywordDto;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeywordDto> getQslAllByAuthorId(Long authorId);
}
