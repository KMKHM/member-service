package com.lucid.userservice.controller.request;

import com.lucid.userservice.service.request.SignupDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, message = "비밀번호는 8자 이상입니다.")
    private String password;

    @NotBlank
    @Size(min = 2, message = "닉네임은 2자 이상입니다.")
    private String username;

    public SignupDto toServiceDto() {
        return SignupDto.builder()
                .email(email)
                .password(password)
                .username(username)
                .build();
    }
}
