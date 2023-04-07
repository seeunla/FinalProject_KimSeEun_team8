package com.ll.exam.sen_books.app.hashTag.service;

import com.ll.exam.sen_books.app.hashTag.entity.HashTag;
import com.ll.exam.sen_books.app.hashTag.repository.HashTagRepository;
import com.ll.exam.sen_books.app.keyword.entity.Keyword;
import com.ll.exam.sen_books.app.keyword.service.KeywordService;
import com.ll.exam.sen_books.app.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final KeywordService keywordService;
    private final HashTagRepository hashTagRepository;

    public void applyHashTags(Post post, String keywords) {
        // 기존 해시태그 가져오기
        List<HashTag> oldHashTags = findByPostId(post.getId());

        // 새로운 해시태그 키워드 리스트
        List<String> keywordContents = Arrays.stream(keywords.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        // 삭제할 해시태그 구하기
        List<HashTag> deleteHashTags = new ArrayList<>();
        for(HashTag oldHash : oldHashTags) {
            boolean contains = keywordContents.stream().anyMatch(s -> s.equals(oldHash.getKeyword().getContent()));

            if (!contains) {
                deleteHashTags.add(oldHash);
            }
        }

        // 삭제할 해시태그 삭제
        deleteHashTags.forEach(hashTag -> {
            hashTagRepository.delete(hashTag);
        });

        // 나머지 해시태그는 저장
        keywordContents.forEach(keywordContent -> {
            saveHashTag(post, keywordContent);
        });
    }

    public List<HashTag> findByPostId(long postId) {
        return hashTagRepository.findByPostId(postId);
    }

    //해시태그 저장
    public HashTag saveHashTag(Post post, String keywordContent) {
        Keyword keyword = keywordService.save(keywordContent);

        HashTag opHashTag = hashTagRepository.findByPostIdAndKeywordId(post.getId(), keyword.getId()).orElse(null);

        if (opHashTag != null) {
            return opHashTag;
        }

        opHashTag = HashTag.builder()
                .member(post.getAuthor())
                .post(post)
                .keyword(keyword)
                .build();

        hashTagRepository.save(opHashTag);

        return opHashTag;
    }

    public List<HashTag> getHashTags(Post post) {
        return hashTagRepository.findAllByPostId(post.getId());
    }
}
