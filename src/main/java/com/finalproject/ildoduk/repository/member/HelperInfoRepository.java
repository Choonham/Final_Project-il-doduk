package com.finalproject.ildoduk.repository.member;

import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.QHelperInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;


public interface HelperInfoRepository extends JpaRepository<HelperInfo, Long>, QuerydslPredicateExecutor<HelperInfo> {

    Optional<HelperInfo> findById(String memberId);


    // =====================BLOG==================//

    // 검색 지역에서 활동하는 헬퍼 목록 불러오기
    @Query(value = "select h, m from HelperInfo h join Member m on h.memberId.id = m.id and m.id in (select distinct b.helper.id from BiddingList b where b.aucSeq.aucSeq in (select a.aucSeq from AuctionList a where a.sigungu = :sigungu))")
    Page<Object[]> selectDistinctBySigungu(Pageable pagable, String sigungu);
    // 검색 지역에서 활동하는 헬퍼 숫자 불러오기
    @Query(value = "select count(h) from HelperInfo h where h.memberId.id in (select distinct b.helper.id from BiddingList b where b.aucSeq.aucSeq in (select a.aucSeq from AuctionList a where a.sigungu = :sigungu))")
    int countDistinctBySigungu(String sigungu);


}
