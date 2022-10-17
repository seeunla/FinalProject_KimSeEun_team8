package com.ll.exam.sen_books.app.member.service;

import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.member.repository.MemberRepository;
import com.ll.exam.sen_books.util.mail.dto.ResponseMessage;
import com.ll.exam.sen_books.util.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public Member join(String username, String password, String email, String ninckname) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException();
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(ninckname)
                .build();

        memberRepository.save(member);

        try {
            sendJoinEmail(member) ;
        } catch (MessagingException e) {
            throw new RuntimeException();
        };

        return member;
    }

    public void sendJoinEmail(Member member) throws MessagingException {
        String message = "SenEbook 회원 가입을 진심으로 축하드립니다.";

        ResponseMessage responseMessage = ResponseMessage.builder()
                .to(member.getEmail())
                .subject("SenEbook 회원 가입 안내")
                .message(message
                        + " \n 바로가기링크 "
                        + " \n http://localhost:8010 ")
                .build();

        emailService.sendEmail(responseMessage);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).get();
    }
}
