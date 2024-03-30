package com.lucid.userservice.service;

import com.lucid.userservice.service.request.SignupDto;
import com.lucid.userservice.service.response.EmailVerificationResponse;
import com.lucid.userservice.service.response.MemberResponse;
import com.lucid.userservice.util.ApiResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService {
    MemberResponse signup(SignupDto request);
    MemberResponse info();
    void logout(String refreshToken, String accessToken);
    void sendCode(String email);
    ApiResponse<EmailVerificationResponse> verifyCode(String email, String code);
}
