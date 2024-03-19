package com.lucid.userservice.service;

import com.lucid.userservice.service.request.SignupRequest;
import com.lucid.userservice.service.response.MemberResponse;

public interface MemberService {
    MemberResponse signup(SignupRequest request);
}
