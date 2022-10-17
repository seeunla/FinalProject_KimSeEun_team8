package com.ll.exam.sen_books.app.security.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.ll.exam.sen_books.app.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@JsonIncludeProperties
public class MemberContext extends User {
    private final Long id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final String username;
    private final String email;
    private final Set<GrantedAuthority> authorities;

    public MemberContext(Member member) {
        super(member.getUsername(), member.getPassword(), member.getAuthorities());

        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.authorities = member.getAuthorities().stream().collect(Collectors.toSet());
    }

    public Member getMember() {
        return Member
                .builder()
                .id(id)
                .createDate(createDate)
                .modifyDate(modifyDate)
                .username(username)
                .email(email)
                .build();
    }

    public String getName() {
        return getUsername();
    }
}
