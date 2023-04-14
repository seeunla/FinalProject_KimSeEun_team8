package com.ll.exam.sen_books.app.postKeyword.service;

import com.ll.exam.sen_books.app.postKeyword.dto.PostKeywordDto;
import com.ll.exam.sen_books.app.postKeyword.entity.PostKeyword;
import com.ll.exam.sen_books.app.postKeyword.exception.PostKeywordNotFoundException;
import com.ll.exam.sen_books.app.postKeyword.repository.PostKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // 작가가 등록한 글과 관련된 모든 해시태그 키워드 조회
    public List<PostKeywordDto> findByMemberId(Long authorId) {
        return postKeywordRepository.getQslAllByAuthorId(authorId);
    }

    public PostKeyword findById(Long id) {
        return postKeywordRepository.findById(id).orElseThrow(() -> {
            throw new PostKeywordNotFoundException("해당 키워드는 존재하지 않습니다.");
        });
    }
}
