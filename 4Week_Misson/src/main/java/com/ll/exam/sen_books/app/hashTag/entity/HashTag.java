package com.ll.exam.sen_books.app.hashTag.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.keyword.entity.PostKeyword;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class HashTag extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Post post;
    @ManyToOne(fetch = LAZY)
    private PostKeyword postKeyword;
    @ManyToOne(fetch = LAZY)
    private Member member;

    // postHashTag 키워드로 게시글 검색 요청 url
    public String getSearchUrl() {
        String url = "/post/list?kwType=hashTag&kw=%s".formatted(this.getPostKeyword().getContent());
        return url;
    }
}
