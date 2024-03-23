package com.lucid.userservice.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucid.userservice.config.jwt.TokenDto;
import com.lucid.userservice.config.jwt.TokenProvider;
import com.lucid.userservice.config.redis.RedisService;
import com.lucid.userservice.controller.request.LoginRequest;
import com.lucid.userservice.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {

        LoginRequest loginReq = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginReq.getEmail(), loginReq.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String userName = ((User) authResult.getPrincipal()).getUsername();

        TokenDto tokenDto = tokenProvider.generateToken(authResult);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        tokenProvider.setAccessTokenHeader(response, accessToken);
        tokenProvider.setRefreshTokenHeader(response, refreshToken);

        log.info(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("userName = {}", userName);

        long refreshTokenExpirationMillis = tokenProvider.getRefreshTokenExpirationMillis();
        redisService.setValues(userName, refreshToken, Duration.ofMillis(refreshTokenExpirationMillis));

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
