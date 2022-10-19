package com.ll.exam.sen_books.app.hashTag.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.keyword.entity.Keyword;
import com.ll.exam.sen_books.app.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class HashTag extends BaseEntity {
    @ManyToOne
    private Post post;
    @ManyToOne
    private Keyword keyword;
}
