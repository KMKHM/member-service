package com.lucid.userservice.controller;

import com.lucid.userservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/home")
    public String home() {
        return "hi";
    }
}
