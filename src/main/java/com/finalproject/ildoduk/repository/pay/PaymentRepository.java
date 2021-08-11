package com.finalproject.ildoduk.repository.pay;


import com.finalproject.ildoduk.dto.auction.AuctionListDTO;
import com.finalproject.ildoduk.entity.pay.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, QuerydslPredicateExecutor<Payment> {

    Page<Payment> findAllByUserIdId(String id, Pageable pageable);

    Optional<Payment> findAllByPointNo(Long pointNo);


//-----------------------------   경매 결제 관련 -----------------------------
    //1. 경매글 등록시에 처음 제시한 금액만큼 포인트 차감
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Member m SET m.point = m.point - ?1 WHERE m.id = ?2")
    void minusPointRegAuction(int startPrice,String userId);
    //   경매 매칭 실패 : 처음 가져간 startPrice만큼 다시 돌려 받기
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Member m SET m.point = m.point + ?1 WHERE m.id = ?2")
    void plusPointNotMatching(int startPrice,String userId);

    //2.경매 매칭 성공 - 차액 만큼 다시 포인트 증가
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Member m SET m.point = m.point + :difference WHERE m.id = :userId")
    void plusPointBidSuccess(int difference,String userId);


}
