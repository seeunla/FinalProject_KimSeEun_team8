package com.ll.exam.sen_books.app.keyword.service;

import com.ll.exam.sen_books.app.keyword.entity.PostKeyword;
import com.ll.exam.sen_books.app.keyword.repository.PostKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostKeywordService {
    private final PostKeywordRepository postKeywordRepository;

    // 키워드 저장
    public PostKeyword save(String keywordContent) {
        PostKeyword opPostKeyword = findByContent(keywordContent);

        if ( opPostKeyword !=null ) {
            return opPostKeyword;
        }

        opPostKeyword = PostKeyword
                .builder()
                .content(keywordContent)
                .build();

        postKeywordRepository.save(opPostKeyword);

        return opPostKeyword;
    }

    // 키워드 content 로 조회

    public PostKeyword findByContent(String content) {
        return postKeywordRepository.findByContent(content).orElse(null);
    }
}
