package com.lucid.userservice.service;

import com.lucid.userservice.service.request.SignupDto;
import com.lucid.userservice.service.response.MemberResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    MemberResponse signup(SignupDto request);
}
