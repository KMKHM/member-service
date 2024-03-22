package com.lucid.userservice.config.security;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


import com.lucid.userservice.config.jwt.TokenProvider;
import com.lucid.userservice.service.MemberService;
import com.lucid.userservice.service.MemberServiceImpl;
import com.lucid.userservice.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailService userDetailService;
    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests.requestMatchers(antMatcher("/sign-up")).permitAll())
                .authorizeHttpRequests((requests) -> requests.requestMatchers(antMatcher("/home")).permitAll())
                .authorizeHttpRequests((requests) -> requests.requestMatchers(antMatcher("/**")).permitAll())
                .authorizeHttpRequests((request) -> {
                    request.requestMatchers(antMatcher("/auth")).authenticated();
                })
                .csrf((csrf) -> csrf.disable())
                .formLogin(f -> f.disable())
                .headers((e) -> e.frameOptions((a) -> a.sameOrigin()))
                .with(new CustomFilterConfigurer(), Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            CustomAuthenticationFilter customAuthenticationFilter =
                    new CustomAuthenticationFilter(authenticationManager, userDetailService, tokenProvider);
            customAuthenticationFilter.setFilterProcessesUrl("/login");
            builder.addFilter(customAuthenticationFilter);
        }
    }

}
