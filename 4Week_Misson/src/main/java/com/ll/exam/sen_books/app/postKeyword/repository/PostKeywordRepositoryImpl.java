package com.ll.exam.sen_books.app.postKeyword.repository;

import com.ll.exam.sen_books.app.postKeyword.dto.PostKeywordDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.exam.sen_books.app.hashTag.entity.QHashTag.hashTag;
import static com.ll.exam.sen_books.app.postKeyword.entity.QKeyword.keyword;

@RequiredArgsConstructor
public class PostKeywordRepositoryImpl implements PostKeywordRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostKeywordDto> getQslAllByAuthorId(Long authorId) {
        // 생성자를 이용해 List<PostKeywordDto> 로 반환
        return jpaQueryFactory
                .select(Projections.constructor(PostKeywordDto.class,
                        keyword.id,
                        keyword.content,
                        hashTag.count()
                ))
                .from(keyword)
                .innerJoin(hashTag)
                .on(keyword.eq(hashTag.keyword))
                .where(hashTag.member.id.eq(authorId))
                .orderBy(keyword.content.asc()) // 키워드 명 오름차순 정렬
                .groupBy(keyword.id)            // 키워드 중복 제거
                .fetch();
    }
}
