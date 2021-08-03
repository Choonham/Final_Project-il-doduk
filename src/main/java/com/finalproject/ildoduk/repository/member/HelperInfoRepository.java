package com.finalproject.ildoduk.repository.member;

import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface HelperInfoRepository extends JpaRepository<HelperInfo, Long>, QuerydslPredicateExecutor<HelperInfo> {

    // =====================BLOG==================//

    // 검색 지역에서 활동하는 헬퍼 목록 불러오기
    @Query(value = "select h, m from HelperInfo h, Member m where h.memberId.id in (select distinct b.helper.id from BiddingList b where b.aucSeq.aucSeq in (select a.aucSeq from AuctionList a where a.sigungu = :sigungu)) and m.id in (select distinct b.helper.id from BiddingList b where b.aucSeq.aucSeq in (select a.aucSeq from AuctionList a where a.sigungu = :sigungu))")
    Page<Object[]> selectDistinctBySigungu(Pageable pagable, String sigungu);
}
