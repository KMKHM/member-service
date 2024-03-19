package com.lucid.userservice.service;

import com.lucid.userservice.repository.MemberRepository;
import com.lucid.userservice.service.request.SignupRequest;
import com.lucid.userservice.service.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public MemberResponse signup(SignupRequest request) {
        return null;
    }
}
