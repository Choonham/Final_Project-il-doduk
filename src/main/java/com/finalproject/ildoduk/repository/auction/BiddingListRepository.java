package com.finalproject.ildoduk.repository.auction;

import com.finalproject.ildoduk.entity.auction.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Repository
@Transactional
public interface BiddingListRepository extends JpaRepository<BiddingList, Long> {

    //옥션이 삭제된 경우 관련 경매내역은 chosen이 2으로 변경
    @Modifying
    @Query(value = "update BiddingList b set b.chosen=2 where b.aucSeq.aucSeq=:aucSeq")
    void deleteAuction(Long aucSeq);

    //옥션값에 따른 비딩내역 불러오기
    @Query(value = "select b from BiddingList b where b.chosen=0 and b.aucSeq.aucSeq = :aucSeq")
    Page<BiddingList> selectByAucSeq(Pageable pageable, Long aucSeq);

    //옥션 값에 따른 비딩 내역 중 낙찰 내역 불러오기
    @Query(value = "select b from BiddingList b where b.chosen=1 and b.aucSeq.aucSeq = :aucSeq")
    Optional<BiddingList> selectByAucSeq2(Long aucSeq);



}
