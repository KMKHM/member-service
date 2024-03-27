package com.lucid.userservice.config.oauth;

import com.lucid.userservice.config.jwt.TokenDto;
import com.lucid.userservice.config.jwt.TokenProvider;
import com.lucid.userservice.config.oauth.response.CustomOAuth2User;
import com.lucid.userservice.config.redis.RedisService;
import com.lucid.userservice.domain.Member;
import com.lucid.userservice.domain.Role;
import com.lucid.userservice.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            log.info(oAuth2User.getEmail());
            log.info(oAuth2User.getName().toString());


            // 사용자 권한
            String authorities = oAuth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
            log.info(authorities);

            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
            if (oAuth2User.getRole() == Role.ROLE_GUEST) {
//                response.sendRedirect("https://localhost:8080/oauth2/sign-up");
                log.info("AA");
            } else {
                // 이미 가입된 회원의 경우 바로 토큰 발급해주면 된다.
                TokenDto tokenDto = tokenProvider.generateTokenDtoOAuth(oAuth2User.getEmail(), authorities);
                tokenProvider.setAccessTokenHeader(response, tokenDto.getAccessToken());
                tokenProvider.setRefreshTokenHeader(response, tokenDto.getRefreshToken());


                long refreshTokenExpirationMillis = tokenProvider.getRefreshTokenExpirationMillis();
                redisService.setValues(oAuth2User.getEmail(), tokenDto.getRefreshToken(), Duration.ofMillis(refreshTokenExpirationMillis));
            }
        } catch (Exception e) {
            throw e;
        }

    }
}