package com.lucid.userservice.service;

import com.lucid.userservice.config.security.SecurityUtil;
import com.lucid.userservice.domain.Member;
import com.lucid.userservice.repository.MemberRepository;
import com.lucid.userservice.service.request.SignupDto;
import com.lucid.userservice.service.response.MemberResponse;

import java.util.Collections;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResponse signup(SignupDto request) {

        if (!checkDuplicateEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        Member member = request.toEntity();
        member.passwordEncoding(passwordEncoder);

        return MemberResponse.of(memberRepository.save(member));
    }

    @Override
    public MemberResponse info() {
        log.info(SecurityUtil.getCurrentMemberEmail());
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    private boolean checkDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            return false;
        }
        return true;
    }

}
