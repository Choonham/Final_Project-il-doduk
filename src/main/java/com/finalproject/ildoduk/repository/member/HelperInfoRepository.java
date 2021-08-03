package com.finalproject.ildoduk.repository.member;

import com.finalproject.ildoduk.entity.member.HelperInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface HelperInfoRepository extends JpaRepository<HelperInfo, Long> {

    // =====================BLOG==================//

    // 검색 지역에서 활동하는 헬퍼 목록 불러오기
    @Query(value = "select h from HelperInfo h where h.memberId.id = (select distinct b.helper.id from BiddingList b, AuctionList a " +
            "where b.aucSeq.aucSeq = (select a.aucSeq from AuctionList a where a.sigungu = :sigungu))")
    Page<HelperInfo> selectDistinctBySigungu(Pageable pagable, String sigungu);
}
