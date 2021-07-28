package com.finalproject.ildoduk.repository.member;

import com.finalproject.ildoduk.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByIdAndPwd(String id, String pwd);
    Optional<Member> findById(String id);

}
