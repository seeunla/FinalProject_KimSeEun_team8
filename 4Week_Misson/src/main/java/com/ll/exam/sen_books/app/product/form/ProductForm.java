package com.ll.exam.sen_books.app.product.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductForm {
    @NotNull
    private String subject;
    @NotNull
    private int price;
    @NotNull
    private long postId;

}
