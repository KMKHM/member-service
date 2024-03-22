package com.lucid.userservice.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
    private SecurityUtil() {

    }

    public static String getCurrentMemberEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info(authentication.getName().toString());
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("인증정보 없음");
        }

        return authentication.getName();
    }
}
