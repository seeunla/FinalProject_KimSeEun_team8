package com.ll.exam.sen_books.app.member.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ResponseMember {
    private String username;
    private String email;
    private String nickname;
}
