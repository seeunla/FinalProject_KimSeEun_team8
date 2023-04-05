package com.ll.exam.sen_books.app.keyword.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Keyword extends BaseEntity {
    @Column(unique = true)
    private String content;

    public String getListUrl() {
        return "/post/list?kwType=keyword&kw=%s".formatted(content);
    }
}
