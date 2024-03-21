package com.lucid.userservice.controller;

import com.lucid.userservice.controller.request.SignupRequest;
import com.lucid.userservice.service.MemberService;
import com.lucid.userservice.service.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/sign-up")
    public ResponseEntity<MemberResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signup(request.toServiceDto()));
    }

    @GetMapping("/")
    public String load(@RequestParam(name = "email") String email) {
        return memberService.loadUserByUsername(email).toString();
    }
}
