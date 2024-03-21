package com.lucid.userservice.service.response;

import com.lucid.userservice.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponse {
    private String email;
    private String username;

    @Builder
    public MemberResponse(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }
}
