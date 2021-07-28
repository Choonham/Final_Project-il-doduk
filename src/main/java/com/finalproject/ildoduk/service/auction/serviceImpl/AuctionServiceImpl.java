package com.finalproject.ildoduk.service.auction.serviceImpl;


import com.finalproject.ildoduk.dto.*;
import com.finalproject.ildoduk.dto.auction.*;
import com.finalproject.ildoduk.entity.auction.*;
import com.finalproject.ildoduk.entity.member.*;
import com.finalproject.ildoduk.repository.auction.*;
import com.finalproject.ildoduk.repository.member.*;
import com.finalproject.ildoduk.service.auction.service.*;
import com.querydsl.core.*;
import com.querydsl.core.types.dsl.*;
import lombok.*;
import lombok.extern.log4j.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import java.text.*;
import java.time.*;
import java.util.*;
import java.util.function.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionListRepository auctionListRepository;
    private final MemberRepository userRepository;
    private final BiddingListRepository biddingListRepository;

    //Member 정보 얻기
    @Override
    public Member getMember(String id){
        Member member = userRepository.findById(id).get();
        return member;
    }

    //오늘날짜 얻기
    @Override
    public String today() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat stf = new SimpleDateFormat("hh:mm");
        String td = dtf.format(now) + "T" + stf.format(now);
        return td;
    }

    //경매 등록
    @Override
    public Long register(AuctionListDTO dto) {
        System.out.println("======= register ========");
        log.info(dto);
        Member user = this.getMember(dto.getUser());

        AuctionList auctionList = dtoToEntity(dto, user);

        auctionListRepository.save(auctionList);
        return auctionList.getAucSeq();
    }

    //경매 참여
    @Override
    public Long BiddingIn(BiddingListDTO dto) {
        System.out.println("======== bidding in =========");
        log.info(dto);
        Member helper = this.getMember(dto.getHelper());

        BiddingList biddingList = dtoToEntity(dto, helper);
        biddingListRepository.save(biddingList);

        return biddingList.getBidSeq();
    }

    //경매삭제 - 관련 비딩내역 biddingList.chosen = 2로 변경 경매 auctionList.state=4 변경
    @Override
    public void deleteAuction(Long aucSeq) {
        AuctionList auctionList = auctionListRepository.findById(aucSeq).get();
        biddingListRepository.deleteAuction(aucSeq); // 해당 경매 비딩리스트 모두 chosen=2로 변경
        auctionList.changeState(4); //경매내역에서 state = 4로 변경
        auctionListRepository.save(auctionList);
    }

    //경매참여내역 - 비딩내역 biddingList.chosen = 2로 변경
    @Override
    public void deleteBidding(Long bidSeq){
        biddingListRepository.deleteById(bidSeq);
    }

    //낙찰,경매선택 - biddingList.chosen =1, aucitonList.state = 2
    @Override
    public void chooseBidding(Long bidSeq) {
        BiddingList biddingList = biddingListRepository.findById(bidSeq).get();
        biddingList.changeChosen(1);  // 해당 비딩 내역 chosen =1 로 변경
        biddingListRepository.save(biddingList);

        AuctionList auctionList = biddingList.getAucSeq(); // 해당 옥션 값 불러와서 state = 2로 변경
        auctionList.changeState(2);
        auctionListRepository.save(auctionList);
    }

    //aucSeq 옥션 값 하나만 가져오기
    @Override
    public Optional<AuctionList> getAuction(Long aucSeq) {
        System.out.println("======== getAuction =========");
        Optional<AuctionList> auction = auctionListRepository.findById(aucSeq);
        return auction;
    }

    //옥션 값에 따른 비딩정보
    @Override
    public PageResultsDTO<BiddingListDTO, BiddingList> getBidding(PageRequestDTO pageRequestDTO,Long aucSeq){
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("aucSeq").descending());

        Page<BiddingList> result = biddingListRepository.selectByAucSeq(pageable, aucSeq);
        Function<BiddingList, BiddingListDTO> fn = (entity -> entityToDTO(entity));
        return new PageResultsDTO<>(result, fn);
    }

    //옥션값에 따른 낙찰 비딩 정보
    @Override
    public Optional<BiddingList> chosenBidding(Long aucSeq){
        Optional<BiddingList> bidding = biddingListRepository.selectByAucSeq2(aucSeq);
        return bidding;
    }

    //비딩 참여 가능한 옥션리스트 test :state = 0, Auction
   @Override
    public PageResultsDTO<AuctionListDTO, AuctionList> getAvailableAuctions(String sidoP, String sigunguP, int categoryP, PageRequestDTO pageRequestDTO){
        //정렬방식 설정
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("aucSeq").descending());

        BooleanBuilder builder = new BooleanBuilder();

       QAuctionList qAuctionList = QAuctionList.auctionList;
       BooleanExpression basic = qAuctionList.state.in(0); // state가 0인 모든 값
       BooleanExpression sido = qAuctionList.sido.contains(sidoP);
       BooleanExpression sigungu = qAuctionList.sigungu.contains(sigunguP);

       BooleanExpression exAll = basic.and(sido).and(sigungu);
       //카테고리 값이 0이 아니면 빌더 추가
        if(categoryP != 0){
            BooleanExpression category = qAuctionList.category.in(categoryP);
            exAll = exAll.and(category);
        }

        builder.and(exAll);

        Page<AuctionList> result = auctionListRepository.findAll(builder, pageable);
        Function<AuctionList, AuctionListDTO> fn = (entity -> entityToDTO(entity));
        return new PageResultsDTO<>(result, fn);
    }

    //목록처리1 test :state = 0, Auction
    @Override
    public PageResultsDTO<AuctionListDTO, AuctionList> getList1(PageRequestDTO pageRequestDTO, String user) {
        //정렬방식 설정
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("aucSeq").descending());

        Page<AuctionList> result = auctionListRepository.getAllWithState1(pageable, user);
        Function<AuctionList, AuctionListDTO> fn = (entity -> entityToDTO(entity));
        return new PageResultsDTO<>(result, fn);
    }

    //목록처리2 경매 완료, 매칭 미완료 , test :state = 1, Auction-Address
    @Override
    public PageResultsDTO<AuctionListDTO, AuctionList> getList2(PageRequestDTO pageRequestDTO, String user) {

        //정렬방식 설정
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("aucSeq").descending());

        Page<AuctionList> result = auctionListRepository.getAllWithState2(pageable, user);
        Function<AuctionList, AuctionListDTO> fn = (entity -> entityToDTO(entity));
        return new PageResultsDTO<>(result, fn);
    }

    //목록처리3 매칭완료, 일 수행 전 값   state=2(매칭 완료), ischosen=1(낙찰값) , auctionList-Bidding 정보 출력
    @Override
    public PageResultsDTO<AuctionBiddingDTO, Object[]> getList3(PageRequestDTO pageRequestDTO, String user) {
        //정렬방식 설정
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("aucSeq").descending());

        Page<Object[]> result = auctionListRepository.getAllWithState3(pageable, user);
        Function<Object[], AuctionBiddingDTO> fn = (en -> entityToDTO((AuctionList) en[0], (BiddingList) en[1]));
        return new PageResultsDTO<>(result, fn);
    }

    //목록처리4 매칭완료, 일 수행 후   state=3(일 수행완료), ischosen=1(낙찰값) , auctionList-Bidding 정보 출력
    @Override
    public PageResultsDTO<AuctionBiddingDTO, Object[]> getList4(PageRequestDTO pageRequestDTO, String user) {
        //정렬방식 설정
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("aucSeq").descending());

        Page<Object[]> result = auctionListRepository.getAllWithState4(pageable, user);
        Function<Object[], AuctionBiddingDTO> fn = (en -> entityToDTO((AuctionList) en[0], (BiddingList) en[1]));
        return new PageResultsDTO<>(result, fn);
    }

    //타이머 - 경매 남은 시간얻기
    @Override
    public long timer(Long aucSeq) {
        Optional<AuctionList> auction = this.getAuction(aucSeq);
        LocalDateTime time = auction.get().getRegDate(); // 등록시간 불러오기
        Date dTime = java.sql.Timestamp.valueOf(time); // 계산하기 위해 형 변형

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // 지금 시간
        cal.setTime(dTime);
        cal.add(Calendar.HOUR, 1); // 등록시간 +1 : 경매 완료 시간
        Date endTime = cal.getTime();
        long sec = (endTime.getTime() - now.getTime()) / 1000; // 경매완료시간 - 지금시간 (초)
        return sec;
    }

    //타이머 셋팅 - 안써도 됨
/*    @Override
    public void timerSet(PageResultDTO<AuctionListDTO, AuctionList> getList) {
        int n = getList.getDtoList().size();
        if (n > 0) {
            for (int i = 0; i < n; i++) {
                Long seq = getList.getDtoList().get(i).getAucSeq();
                Long time = this.timer(seq);
                getList.getDtoList().get(i).setTimer(time);
            }
        } else {
            System.out.println("해당 내역이 없습니다. ");
        }

    }*/

    //일 시작까지 남은 시간 구하기
    @Override
    public long leftTime(Long aucSeq) {
        Optional<AuctionList> auction = this.getAuction(aucSeq);
        LocalDateTime time = auction.get().getDoDateTime(); //일 시작 시간
        Date dTime = java.sql.Timestamp.valueOf(time);

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime(); // 지금 시간

        long sec = dTime.getTime() - now.getTime() / 1000;
        // sec < 0 일 경우 일 시작시간이 지난 것 !

        return sec;
    }


    //경매시간 완료, 매칭 미완료 경매 state=1로 변경
    @Override
    public void changeState1(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        //정렬방식 설정
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("aucSeq").descending());

        Page<AuctionList> result = auctionListRepository.ChangeState1(pageable);
        Function<AuctionList, AuctionListDTO> fn = (en -> entityToDTO(en));

        PageResultsDTO<AuctionListDTO, AuctionList> sResult = new PageResultsDTO<>(result, fn);

        for (AuctionListDTO dto : sResult.getDtoList()) {
            Long aucSeq = dto.getAucSeq();
            long getTimer = this.timer(aucSeq);
            long getLeftTime = this.leftTime(aucSeq);

            if (getTimer < 0 && getLeftTime > 0) {
                Member user = userRepository.findById(dto.getUser()).get();
                AuctionList auctionList = dtoToEntity(dto,user);
                auctionList.changeState(1);
                auctionListRepository.save(auctionList);
            }
        }
    }

    //경매시간 완료, 매칭미완료, 일 시작시간 초과 경매 state=4로 변경
    @Override
    public void changeState2(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        //정렬방식 설정
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("aucSeq").descending());

        Page<AuctionList> result = auctionListRepository.ChangeState2(pageable);
        Function<AuctionList, AuctionListDTO> fn = (this::entityToDTO);

        PageResultsDTO<AuctionListDTO, AuctionList> sResult = new PageResultsDTO<>(result, fn);

        for (AuctionListDTO dto : sResult.getDtoList()) {
            Long aucSeq = dto.getAucSeq();
            long getTimer = this.timer(aucSeq);
            long getLeftTime = this.leftTime(aucSeq);

            if (getTimer < 0 && getLeftTime < 0) {
                Member user = userRepository.findById(dto.getUser()).get();
                AuctionList auctionList = dtoToEntity(dto,user);
                auctionList.changeState(4);
                auctionListRepository.save(auctionList);
            }
        }

    }

    //일 수행 완료 후 state값 변경
    @Override
    public void jobDone(Long aucSeq) {
        Optional<AuctionList> auction = this.getAuction(aucSeq);
        AuctionList auctionList = auction.get();
        auctionList.changeState(3);
        auctionListRepository.save(auctionList);
    }
}