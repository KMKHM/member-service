package com.lucid.userservice.config.oauth.response;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    // 카카오 로그인의 경우, 이메일을 얻으려면 애플리케이션 심사과정을 거쳐야 함.

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        if (account == null) {
            return null;
        }

        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (profile == null) {
            return null;
        }

        return (String) profile.get("nickname");
    }

}
