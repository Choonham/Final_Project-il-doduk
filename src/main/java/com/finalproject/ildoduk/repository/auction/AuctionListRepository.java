package com.finalproject.ildoduk.repository.auction;

import com.finalproject.ildoduk.dto.auction.*;
import com.finalproject.ildoduk.entity.auction.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.util.*;


@Repository
public interface AuctionListRepository extends JpaRepository<AuctionList, Long>, QuerydslPredicateExecutor<AuctionList> {

    //aucSeq 값으로 옥션 하나만 불러오기
    @Query(value = "select a from AuctionList a where a.aucSeq=:aucSeq")
    Object[] getAuctionWithId(@Param("aucSeq") Long aucSeq);

    //경매 진행 중인 목록 user별 state=0(경매진행 중) , auctionList정보 출력
    @Query(value = "SELECT a from AuctionList a WHERE a.state=0 and a.user.id=:user")
    Page<AuctionList> getAllWithState1(Pageable pageable, String user);

    //경매 완료, 매칭미완료 값
    @Query(value = "SELECT a from AuctionList a WHERE a.state=1 and a.user.id=:user")
    Page<AuctionList> getAllWithState2(Pageable pageable,String user);

    //매칭완료, 일 수행 전 값   state=2(매칭 완료), ischosen=1(낙찰값) , auctionList-Bidding 정보 출력
    @Query(value = "SELECT a,b from AuctionList a , BiddingList b WHERE a.state=2 and a.user.id=:user and a.aucSeq = b.aucSeq.aucSeq and b.chosen =1")
    Page<Object[]> getAllWithState3(Pageable pageable,String user);

    //일 수행 완료 된 값 불러오기 steat=3 (일수행완료) auctionList-Bidding
    @Query(value = "SELECT a,b from AuctionList a ,  BiddingList b WHERE a.state=3 and a.user.id=:user and a.aucSeq = b.aucSeq.aucSeq and b.chosen =1")
    Page<Object[]> getAllWithState4(Pageable pageable, String user);

    // 경매시간 끝난 경매 상태 변경 - regDate + 1h < 0 and state=0 이 있으면 로딩 시 state=1 로 변경
    @Query(value = "select a from AuctionList a where a.state=0")
    Page<AuctionList> ChangeState1(Pageable pageable);

    // 일 시작 시간 초과한 미선택 경매 삭제 - doDateTime < now 인 state=1 값이 있으면 강제로 state = 4로 바꾸는 메서드 필요
    @Query(value = "select a from AuctionList a where a.state=1")
    Page<AuctionList> ChangeState2(Pageable pageable);

    //유저값에 따른 비딩 참여내역 출력 - auction 내역도 보여야 하지 않나,,,?
    @Query(value = "select a,b from AuctionList a, BiddingList b where b.helper.id=:helper and a.aucSeq = b.aucSeq.aucSeq")
    Page<Object[]> getMyBids(Pageable pageable, String helper);

    //====================================== Blog =====================================//

    //헬퍼 기준 일 수행 완료 된 갑 불러오기 state=3, auction-bidding <List>
    @Query(value = "SELECT a,b FROM AuctionList a, BiddingList b WHERE a.state=3 and b.helper.id=:helper and a.aucSeq=b.aucSeq.aucSeq and b.chosen=1")
    List<AuctionBiddingDTO> getAllWith4ForHelper(String helper);
}
