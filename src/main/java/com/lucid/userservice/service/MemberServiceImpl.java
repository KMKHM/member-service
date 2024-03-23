package com.lucid.userservice.service;

import com.lucid.userservice.config.jwt.TokenProvider;
import com.lucid.userservice.config.redis.RedisService;
import com.lucid.userservice.config.security.SecurityUtil;
import com.lucid.userservice.domain.Member;
import com.lucid.userservice.repository.MemberRepository;
import com.lucid.userservice.service.request.SignupDto;
import com.lucid.userservice.service.response.MemberResponse;

import io.jsonwebtoken.Claims;
import java.time.Duration;
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
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

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

    @Override
    public void logout(String refreshToken, String accessToken) {
        String email = tokenProvider.parseClaims(accessToken).getSubject();
        log.info(email);
        String redisRefreshToken = redisService.getValues(email);

        if (redisService.checkExistsValue(redisRefreshToken)) {
            redisService.deleteValues(email);
            log.info("redis");
            long accessTokenExpirationMillis = tokenProvider.getAccessTokenExpirationMillis();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
        } else {
            log.info("null");
        }
    }


    private boolean checkDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            return false;
        }
        return true;
    }

}
