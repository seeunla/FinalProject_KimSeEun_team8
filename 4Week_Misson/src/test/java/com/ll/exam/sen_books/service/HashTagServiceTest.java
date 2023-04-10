package com.ll.exam.sen_books.service;

import com.ll.exam.sen_books.app.hashTag.entity.HashTag;
import com.ll.exam.sen_books.app.hashTag.service.HashTagService;
import com.ll.exam.sen_books.app.keyword.service.PostKeywordService;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.ll.exam.sen_books.app.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HashTagServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private HashTagService hashTagService;
    @Autowired
    private PostKeywordService postKeywordService;

    @Test
    void t1() {
        Post post = postService.findById(1).get();
        String keywordContent1 = "판타지";
        String keywordContent2 = "판타지";
        String keywordContent3 = "소설";
        // when
        hashTagService.saveHashTag(post, keywordContent1);
        hashTagService.saveHashTag(post, keywordContent2);
        hashTagService.saveHashTag(post, keywordContent3);
        // then
        List<HashTag> hashTags = hashTagService.findByPostId(1);

        assertThat(hashTags.size()).isEqualTo(2);
        assertThat(hashTags.get(0).getPostKeyword().getContent()).isEqualTo("판타지");
        assertThat(hashTags.get(1).getPostKeyword().getContent()).isEqualTo("소설");
    }
}
