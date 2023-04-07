package com.ll.exam.sen_books.app.keyword.service;

import com.ll.exam.sen_books.app.keyword.entity.Keyword;
import com.ll.exam.sen_books.app.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    // 키워드 저장
    public Keyword save(String keywordContent) {
        Keyword opKeyword = findByContent(keywordContent);

        if ( opKeyword !=null ) {
            return opKeyword;
        }

        opKeyword = Keyword
                .builder()
                .content(keywordContent)
                .build();

        keywordRepository.save(opKeyword);

        return opKeyword;
    }

    // 키워드 content 로 조회

    public Keyword findByContent(String content) {
        return keywordRepository.findByContent(content).orElse(null);
    }
}
