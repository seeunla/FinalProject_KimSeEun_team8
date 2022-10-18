package com.ll.exam.sen_books.app.member.controller;

import com.ll.exam.sen_books.app.member.dto.ResponseMember;
import com.ll.exam.sen_books.app.member.dto.RequestModifyPw;
import com.ll.exam.sen_books.app.member.entity.Member;
import com.ll.exam.sen_books.app.member.form.JoinForm;
import com.ll.exam.sen_books.app.member.service.MemberService;
import com.ll.exam.sen_books.app.security.dto.MemberContext;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getEmail(), joinForm.getNickname());

        return "redirect:/member/login?msg=" + Ut.url.encode("회원가입이 완료되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberContext.getMember();

        model.addAttribute("member", member);

        return "member/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modify(@AuthenticationPrincipal MemberContext memberContext, ResponseMember member) {

        memberService.modify(memberContext.getUsername(), member);

        memberContext.setModifyDate(memberContext.getModifyDate());
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberContext, memberContext.getPassword(), memberContext.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/member/profile";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/logout")
    public String logout() {

        return "redirect:/member/logout?msg=" + Ut.url.encode("로그아웃이 완료되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberService.findById(memberContext.getId());

        model.addAttribute("member", member);
        return "member/profile";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findUsername")
    public String showFindUsername() {
        return "member/findUsername";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findUsername")
    public String findUsernameByEmail(String email, Model model) {
        Member member = memberService.findByEmail(email);

        model.addAttribute("member", member.getUsername());

        return "member/findUsername";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findPassword")
    public String showFindPassword() {
        return "member/findUserPassword";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String SendPasswordEmail(ResponseMember member) throws MessagingException {
        memberService.sendPasswordEmail(member);

        return "redirect:/?msg=" + Ut.url.encode( "임시 비밀번호가 %s로 발송되었습니다.".formatted(member.getEmail()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modifyPassword")
    public String showModifyPassword(Model model) {
        RequestModifyPw requestModifyPw = new RequestModifyPw();

        model.addAttribute("modifyPassword", requestModifyPw);

        return "member/modifyPassword";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modifyPassword")
    public String modifyPassword(@Valid RequestModifyPw pw, @AuthenticationPrincipal MemberContext memberContext) {
        if (!(memberService.isEqualPassword(memberContext.getUsername(), pw.getPassword()))) {
            return "redirect:/member/modifyPassword?msg=" + Ut.url.encode( "기존 비밀번호가 일치하지 않습니다.");
        }

        memberService.updatePassword(memberContext.getUsername(), pw.getPassword());

        return "redirect:/?msg=" + Ut.url.encode( "비밀번호가 변경되었습니다.");
    }
}
