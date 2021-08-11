package com.finalproject.ildoduk.repository.member;

import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.member.QHelperInfo;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;


public interface HelperInfoRepository extends JpaRepository<HelperInfo, Long>, QuerydslPredicateExecutor<HelperInfo> {

    Optional<HelperInfo> findByMemberId(Member memberId);

    Optional<HelperInfo> findByMemberId_Id(String memeberId);

    int countHelperInfoByMemberId(Member memeberId);


    // =====================BLOG==================//

    // 검색 지역에서 활동하는 헬퍼 목록 불러오기
    @Query(value = "select h, m from HelperInfo h join Member m on h.memberId.id = m.id and m.id in (select distinct b.helper.id from BiddingList b where b.aucSeq.aucSeq in (select a.aucSeq from AuctionList a where a.sigungu = :sigungu))")
    Page<Object[]> selectDistinctBySigungu(Pageable pagable, String sigungu);
    // 검색 지역에서 활동하는 헬퍼 숫자 불러오기
    @Query(value = "select count(h) from HelperInfo h where h.memberId.id in (select distinct b.helper.id from BiddingList b where b.aucSeq.aucSeq in (select a.aucSeq from AuctionList a where a.sigungu = :sigungu))")
    int countDistinctBySigungu(String sigungu);

    //멤버, 헬퍼 정보 전체 조인인
    @Query(value = "SELECT m,h FROM Member m join HelperInfo h on m.id = h.memberId.id WHERE h.memberId.id = ?1")
    Optional<Object[]> joinHelperInfo(String memberId);

    //헬퍼 신청 agreeHelper : 1(헬퍼 신청)
    @Query(value = "SELECT h FROM HelperInfo h WHERE h.agreeHelper = 1")
    Page<HelperInfo> findAllOne(Pageable pageable);
    //헬퍼 신청 agreeHelper : 2(헬퍼 승인 완료)
    @Query(value = "SELECT h FROM HelperInfo h WHERE h.agreeHelper = 2")
    Page<HelperInfo> findAllTwo(Pageable pageable);
    //헬퍼 신청 agreeHelper : 3(헬퍼 신청 반려)
    @Query(value = "SELECT h FROM HelperInfo h WHERE h.agreeHelper = 3")
    Page<HelperInfo> findAllThree(Pageable pageable);
}
