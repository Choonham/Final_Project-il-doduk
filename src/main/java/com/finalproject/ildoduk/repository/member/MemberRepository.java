package com.finalproject.ildoduk.repository.member;

import com.finalproject.ildoduk.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByIdAndPwd(String id, String pwd);
<<<<<<< HEAD

    public List<Member> findAll();
=======
    Optional<Member> findById(String id);

<<<<<<< HEAD
>>>>>>> 1819486034e26e29565cb8847b2de832f0a54a02
=======

    //결제 관련

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Member u SET u.point = u.point + ?1 WHERE u.id = ?2")
    void pointUpdate(int userCash,String userID);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Member u SET u.point = ?1 WHERE u.id = ?2")
    void pointMinus(int userCash,String userID);

>>>>>>> 877223ba161a89a8fdcf774c93c8912fcb3a232d
}
