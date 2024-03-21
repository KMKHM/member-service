package com.lucid.userservice.service;

import com.lucid.userservice.service.request.SignupDto;
import com.lucid.userservice.service.response.MemberResponse;

public interface MemberService {
    MemberResponse signup(SignupDto request);
}
