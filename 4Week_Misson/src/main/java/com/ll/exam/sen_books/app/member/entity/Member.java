package com.ll.exam.sen_books.app.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.app.member.entity.emum.AuthLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Slf4j
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String nickname;
    @Convert(converter = AuthLevel.Converter.class)
    private AuthLevel authLevel;
    private long restCash;


    public Member(long id) {
        super(id);
        this.authLevel = getAuthLevel();
    }

    public String getName() {
        return username;
    }

    public void modify(String username, String nickname, String email) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
    }

    public void modifyNickName(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public List<GrantedAuthority> genAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        if (StringUtils.hasText(nickname)) {
            authorities.add(new SimpleGrantedAuthority("AUTHOR"));
        }

        if (authLevel.getCode() >= 7) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }

        log.debug(authorities.toString());

        return authorities;
    }

    public void setRestCash(long newRestCash) {
        this.restCash = newRestCash;
    }
}
