package com.ll.exam.sen_books.app.post.repository;

import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.post.entity.Post;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.exam.sen_books.app.hashTag.entity.QHashTag.hashTag;
import static com.ll.exam.sen_books.app.keyword.entity.QKeyword.keyword;
import static com.ll.exam.sen_books.app.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> searchQsl(Member author, String kwType, String kw) {
        // 1. 전체 조회(키워드가 없거나 검색타입이 검색 타입이 hashTag 가 아닌 경우)
        JPAQuery<Post> jpqQuery = jpaQueryFactory
                .select(post)
                .where(post.author.id.eq(author.getId()))
                .distinct()
                .from(post);

        // 2. 키워드 기반 검색(키워드가 존재하고 키워드 타입이 해시태그인 경우)
        if(!kw.equals("")) {
            if(kwType.equals("hashTag")) {
                jpqQuery
                        .innerJoin(hashTag)
                        .on(post.eq(hashTag.post))
                        .innerJoin(keyword)
                        .on(keyword.eq(hashTag.keyword))
                        .where(keyword.content.eq(kw));
            }
        }
        jpqQuery.orderBy(post.id.desc());

        return jpqQuery.fetch();
    }
}
