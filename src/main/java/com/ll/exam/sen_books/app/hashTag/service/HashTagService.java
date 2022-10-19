package com.ll.exam.sen_books.app.hashTag.service;

import com.ll.exam.sen_books.app.hashTag.entity.HashTag;
import com.ll.exam.sen_books.app.hashTag.repository.HashTagRepository;
import com.ll.exam.sen_books.app.keyword.entity.Keyword;
import com.ll.exam.sen_books.app.keyword.service.KeywordService;
import com.ll.exam.sen_books.app.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final KeywordService keywordService;
    private final HashTagRepository hashTagRepository;

    public void applyHashTags(Post post, String hashTagContents) {
        List<String> keywordContents = Arrays.stream(hashTagContents.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        keywordContents.forEach(keywordContent -> {
            saveHashTag(post, keywordContent);
        });
    }

    private HashTag saveHashTag(Post post, String keywordContent) {
        Keyword keyword = keywordService.save(keywordContent);

        Optional<HashTag> opHashTag = hashTagRepository.findByPostIdAndKeywordId(post.getId(), keyword.getId());

        if (opHashTag.isPresent()) {
            return opHashTag.get();
        }

        HashTag hashTag = HashTag.builder()
                .post(post)
                .keyword(keyword)
                .build();

        hashTagRepository.save(hashTag);

        return hashTag;
    }

    public List<HashTag> getHashTags(Post post) {
        return hashTagRepository.findAllByPostId(post.getId());
    }
}
