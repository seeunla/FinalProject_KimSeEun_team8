package com.ll.exam.sen_books.app.post.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PostForm {
    private String subject;
    private String content;
}
