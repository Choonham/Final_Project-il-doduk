package com.finalproject.ildoduk.repository.member;

import com.finalproject.ildoduk.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {


    //회원관리 - DB검색(nickname 중복체크)
    int countByNickname(String nickname);


    public List<Member> findAll();
    Optional<Member> findById(String id);
    Optional<Member> findAllByNickname(String nick);



    //결제 관련
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Member u SET u.point = u.point + ?1 WHERE u.id = ?2")
    void pointUpdate(int userCash,String userID);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Member u SET u.point = ?1 WHERE u.id = ?2")
    void pointMinus(int userCash,String userID);

}
