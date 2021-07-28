package com.finalproject.ildoduk.repository.member;

import com.finalproject.ildoduk.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByIdAndPwd(String id, String pwd);
<<<<<<< HEAD

    public List<Member> findAll();
=======
    Optional<Member> findById(String id);

>>>>>>> 1819486034e26e29565cb8847b2de832f0a54a02
}
