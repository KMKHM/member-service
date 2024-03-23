package com.lucid.userservice.service;

import com.lucid.userservice.service.request.SignupDto;
import com.lucid.userservice.service.response.MemberResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService {
    MemberResponse signup(SignupDto request);
    MemberResponse info();
    void logout(String refreshToken, String accessToken);
}
