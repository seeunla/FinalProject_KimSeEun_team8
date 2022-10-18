package com.ll.exam.sen_books.app.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.exam.sen_books.app.base.entity.BaseEntity;
import com.ll.exam.sen_books.util.Ut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String nickname;
    private int authLevel;


    public Member(long id) {
        super(id);
    }

    public String getName() {
        return username;
    }

    public void modify(String username, String nickname, String email) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
