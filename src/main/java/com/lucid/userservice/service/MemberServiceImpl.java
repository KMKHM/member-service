package com.lucid.userservice.service;

import static com.lucid.userservice.domain.Role.ROLE_USER;

import com.lucid.userservice.config.jwt.TokenProvider;
import com.lucid.userservice.config.mail.MailService;
import com.lucid.userservice.config.redis.RedisService;
import com.lucid.userservice.config.security.SecurityUtil;
import com.lucid.userservice.domain.Member;
import com.lucid.userservice.repository.MemberRepository;
import com.lucid.userservice.service.request.SignupDto;
import com.lucid.userservice.service.response.EmailVerificationResponse;
import com.lucid.userservice.service.response.MemberResponse;

import com.lucid.userservice.util.ApiResponse;
import io.jsonwebtoken.Claims;
import java.time.Duration;
import java.util.Collections;
import java.util.Random;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final MailService mailService;

    @Value("${mail.expiration}")
    private long authCodeExpirationMills;

    @Value("${mail.length}")
    private int length;

    @Value("${mail.chars}")
    private String characters;

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

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
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail());
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

    @Override
    public void sendCode(String email) {
        String title = "이메일 인증 번호";
        String authCode = createCode();

        mailService.sendEmail(email, title, authCode);

        redisService.setValues(AUTH_CODE_PREFIX + email, authCode, Duration.ofMillis(authCodeExpirationMills));
    }

    // 인증번호 검증
    @Transactional
    @Override
    public ApiResponse<EmailVerificationResponse> verifyCode(String email, String code) {
        if (checkCode(email, code)) {
            Member findMember = memberRepository.findByEmail(email);
            findMember.updateRole(ROLE_USER);
            return ApiResponse.of(HttpStatus.OK, "이메일 인증 성공", EmailVerificationResponse.of(true));
        }
        return ApiResponse.of(HttpStatus.FORBIDDEN, "이메일 인증 실패", EmailVerificationResponse.of(false));
    }

    private boolean checkCode(String email, String code) {
        return code.equals(redisService.getValues(AUTH_CODE_PREFIX + email));
    }

    // 인증번호 생성로직
    private String createCode() {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }


    private boolean checkDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email) != null) {
            return false;
        }
        return true;
    }

}
