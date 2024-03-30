package com.lucid.userservice.repository;

import com.lucid.userservice.domain.Member;
import com.lucid.userservice.domain.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Member findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Member findBySocialId(String socialId);
}
