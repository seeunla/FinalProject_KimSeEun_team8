package com.ll.exam.sen_books.app.product.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductForm {
    @NotNull
    private String subject;
    @NotEmpty
    private String content;
    @NotNull
    private int price;
    @NotNull
    private long postKeywordId;
    private String productKeywords; // 도서(상품) 해시태그 키워드
}
