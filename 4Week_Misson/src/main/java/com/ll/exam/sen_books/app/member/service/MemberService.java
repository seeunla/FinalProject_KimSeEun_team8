package com.ll.exam.sen_books.app.member.service;

import com.ll.exam.sen_books.app.cash.entity.CashLog;
import com.ll.exam.sen_books.app.cash.service.CashService;
import com.ll.exam.sen_books.app.member.controller.MemberController;
import com.ll.exam.sen_books.app.member.dto.ResponseMember;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.member.entity.emum.AuthLevel;
import com.ll.exam.sen_books.app.member.repository.MemberRepository;
import com.ll.exam.sen_books.app.product.form.ModifyForm;
import com.ll.exam.sen_books.app.security.dto.MemberContext;
import com.ll.exam.sen_books.util.mail.dto.ResponseMessage;
import com.ll.exam.sen_books.util.mail.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final CashService cashService;

    public Member join(String username, String password, String email, String nickname) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException();
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .authLevel(AuthLevel.NORMAL)
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


    @Transactional
    public AddCashDataBody addCash(Member member, long price, String eventType) {
        CashLog cashLog = cashService.addCash(member, price, eventType);

        int newRestCash = member.getRestCash() + cashLog.getPrice();
        member.setRestCash(newRestCash);
        memberRepository.save(member);

        return new AddCashDataBody(cashLog, newRestCash);
    }

    @Data
    @AllArgsConstructor
    public static class AddCashDataBody {
        CashLog cashLog;
        long newRestCash;
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }
    @Transactional
    public void modify(Member member, ModifyForm modifyForm) {
        member.modify(modifyForm.getUsername(), modifyForm.getEmail(), modifyForm.getNickname());
        memberRepository.save(member);
        forceAuthentication(member);
    }

    // 세션에 담긴 회원 기본정보 강제 수정
    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member, member.genAuthorities());

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        member.getPassword(),
                        memberContext.getAuthorities()
                );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Transactional
    public Member findByEmail(String email) {
        Member member = memberRepository.findByEmail(email).get();

        return member;
    }

    @Transactional
    public void updatePassword(String username, String password ) {
        Member member = memberRepository.findByUsername(username).get();
        member.updatePassword(passwordEncoder.encode(password));
    }

    public void sendPasswordEmail(ResponseMember responseMember) throws MessagingException {
        String message = "SenEbook 임시 비밀번호입니다.";

        Member member = memberRepository.findByUsername(responseMember.getUsername()).get();

        String tmpPassword = UUID.randomUUID().toString().replace("-", "");
        tmpPassword = tmpPassword.substring(0, 10);

        updatePassword(member.getUsername(), passwordEncoder.encode(tmpPassword));

        ResponseMessage responseMessage = ResponseMessage.builder()
                .to(member.getEmail())
                .subject("임시 비밀번호 발송")
                .message(message + tmpPassword)
                .build();

        emailService.sendEmail(responseMessage);

    }

    public boolean isEqualPassword(String username, String password) {
        Member member = memberRepository.findByUsername(username).get();

        return passwordEncoder.matches(password, member.getPassword());
    }

    public int getRestCash(Member member) {
        return memberRepository.findById(member.getId()).get().getRestCash();
    }

}
