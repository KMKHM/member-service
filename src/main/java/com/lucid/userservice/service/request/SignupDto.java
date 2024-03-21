package com.lucid.userservice.service.request;

import static com.lucid.userservice.domain.Role.*;

import com.lucid.userservice.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupDto {
    private String email;
    private String password;
    private String username;

    @Builder
    private SignupDto(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .username(username)
                .role(ROLE_GUEST)
                .build();
    }
}
