package com.ll.exam.sen_books.app.rebate.form;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class RebateDataForm {
    @NotNull(message = "년도를 선택해주세요.")
    private int year;
    @NotNull(message = "월을 선택해주세요.")
    private int month;
}
