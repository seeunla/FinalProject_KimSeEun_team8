package com.ll.exam.sen_books.app.post.form;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class PostForm {
    @NotEmpty
    private String subject;
    @NotEmpty
    private String content;
    private String contentHtml;
    private String keywords;
}
