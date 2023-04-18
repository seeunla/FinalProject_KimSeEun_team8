//package com.ll.exam.sen_books.app.apimember.controller;
//
//import com.ll.exam.sen_books.app.apimember.dto.LoginDTO;
//import com.ll.exam.sen_books.app.base.dto.RsData;
//import com.ll.exam.sen_books.app.member.entity.Member;
//import com.ll.exam.sen_books.app.apimember.service.ApiMemberService;
//import com.ll.exam.sen_books.app.member.service.MemberService;
//import com.ll.exam.sen_books.app.security.dto.MemberContext;
//import com.ll.exam.sen_books.util.Ut;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/member")
//@RequiredArgsConstructor
//public class ApiMemberController {
//    private final MemberService memberService;
//    private final ApiMemberService apiMemberService;
//
//    private final PasswordEncoder passwordEncoder;
//
//    @PostMapping("/login")
//    public ResponseEntity<RsData> login(@RequestBody LoginDTO loginDTO){
//
//        if(loginDTO.isNotValid()) {
//            return Ut.spring.responseEntityOf(RsData.of("F-1", "로그인 정보가 올바르지 않습니다.."));
//        }
//
//        Member member = memberService.findByUsername(loginDTO.getUsername()).get();
//
//        if(member == null){
//            return Ut.spring.responseEntityOf(RsData.of("F-2", "일치하는 회원이 존재하지 않습니다."));
//        }
//
//        if(passwordEncoder.matches(loginDTO.getPassword(), member.getPassword()) == false){
//            return Ut.spring.responseEntityOf(RsData.of("F-3", "비밀번호가 일치하지 않습니다."));
//        }
//
//        String accessToken = apiMemberService.genAccessToken(member);
//
//        return Ut.spring.responseEntityOf(
//                RsData.of(
//                        "S-1",
//                        "로그인 성공, Access Token을 발급합니다.",
//                        Ut.mapOf(
//                                "accessToken", accessToken
//                        )
//                ),
//                Ut.spring.httpHeadersOf("Authentication", accessToken)
//        );
//    }
//
//    @GetMapping("/me")
//    public ResponseEntity<RsData> test(@AuthenticationPrincipal MemberContext memberContext) {
//        if(memberContext == null) {
//            return Ut.spring.responseEntityOf(RsData.failOf(null));
//        }
//
//        //Member member = memberContext.toDto(memberContext.getMember());
//
//        return Ut.spring.responseEntityOf(RsData.successOf(Ut.mapOf("member")));
//    }
//}
