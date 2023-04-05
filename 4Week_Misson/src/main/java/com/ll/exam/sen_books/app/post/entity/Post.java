package com.ll.exam.sen_books.app.post.entity;

import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.hashTag.entity.HashTag;
import com.ll.exam.sen_books.app.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Post extends BaseEntity {
    private String subject;
    private String content;
    private String contentHtml;


    @ManyToOne(fetch = LAZY)
    private Member author;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private List<HashTag> hashTags = new ArrayList<>();

    public Post(long id) {
        super(id);
    }

    public String getJdenticon() {
        return "post__" + getId();
    }

    public void modify(String subject, String content, String contentHtml) {
        this.subject = subject;
        this.content = content;
        this.contentHtml = contentHtml;
    }
}
