package com.lucid.userservice.controller;

import com.lucid.userservice.config.security.SecurityUtil;
import com.lucid.userservice.controller.request.LoginRequest;
import com.lucid.userservice.controller.request.SignupRequest;
import com.lucid.userservice.domain.Member;
import com.lucid.userservice.repository.MemberRepository;
import com.lucid.userservice.service.MemberService;
import com.lucid.userservice.service.response.MemberResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/")
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

    @GetMapping("/auth")
    public String auth(@RequestBody LoginRequest loginRequest) {
        log.info("email = {}", SecurityUtil.getCurrentMemberEmail());
        return "ok";
    }

    @GetMapping("/login2")
    public String login(@RequestBody LoginRequest loginRequest) {
        log.info("email = {}", SecurityUtil.getCurrentMemberEmail());
        return "ok";
    }

    @GetMapping("/info")
    public ResponseEntity<MemberResponse> info() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.info());
    }
}
