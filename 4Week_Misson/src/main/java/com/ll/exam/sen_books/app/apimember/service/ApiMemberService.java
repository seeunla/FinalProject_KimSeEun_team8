package com.ll.exam.sen_books.app.apimember.service;

import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.apimember.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiMemberService {
    private final JwtProvider jwtProvider;

    public String genAccessToken(Member member) {
        return jwtProvider.generateAccessToken(member.getAccessTokenClaims(), 60 * 60 * 24 * 90);
    }
}
