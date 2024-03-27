//package com.lucid.userservice.controller;
//
//import com.lucid.userservice.config.security.SecurityUtil;
//import com.lucid.userservice.domain.Member;
//import com.lucid.userservice.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//public class TestController {
//    private final MemberRepository memberRepository;
//    @Transactional
//    @PostMapping("/login/oauth2/code/api/oauth2/sign-up")
//    public String aa(@RequestBody SocialSignUpDto req) {
//        String currentMemberId = SecurityUtil.getCurrentMemberEmail();
//        log.info(currentMemberId);
//        return "aa";
//
//    }
//
//}
