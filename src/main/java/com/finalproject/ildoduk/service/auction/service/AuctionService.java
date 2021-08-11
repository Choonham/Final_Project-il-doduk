package com.finalproject.ildoduk.service.auction.service;


import com.finalproject.ildoduk.dto.*;
import com.finalproject.ildoduk.dto.auction.*;
import com.finalproject.ildoduk.entity.auction.*;
import com.finalproject.ildoduk.entity.member.*;
import org.springframework.data.domain.Page;

import java.util.*;

public interface AuctionService {

    //======================================== 공통메서드 ============================================//

    //멤버 얻기
    Member getMember(String id);

    //오늘 날짜 얻기
    String today();

    //타이머 - 경매 남은 시간얻기
    long timer(Long aucSeq);

    //일 시작까지 남은 시간 구하기
    long leftTime(Long aucSeq);

    //경매시간 완료, 매칭 미완료 경매 state=1로 변경
    void changeState1(PageRequestDTO pageRequestDTO);

    //경매시간 완료, 매칭미완료, 일 시작시간 초과 경매 state=4로 변경
    void changeState2(PageRequestDTO pageRequestDTO);

    //======================================== user ============================================//
    //auc.state=0 경매 진행 중 / auc.state=1 경매완료, 매칭미완료 / auc.state=2 매칭완료 / auc.state=3 일 수행 완료 / auc.state=4 삭제

    //경매 등록
    Long register(AuctionListDTO dto);

    //경매삭제 - 관련 비딩내역 biddingList.chosen = 2로 변경 경매 auctionList.state=4 변경
    void deleteAuction(Long aucSeq);

    //경매선택 - biddingList.chosen =1, aucitonList.state = 2
    void chooseBidding(Long bidSeq);

    //aucSe옥션 값 하나만 가져오기
    Optional<AuctionList> getAuction(Long aucSeq);

    AuctionListDTO findAuction(Long aucSeq);

    //옥션 값에 따른 비딩정보
    PageResultsDTO<BiddingListDTO, BiddingList> getBidding(PageRequestDTO pageRequestDTO, Long aucSeq);

    //옥션값에 따른 낙찰 비딩 정보
    Optional<BiddingList> chosenBidding(Long aucSeq);

    //목록처리1 test :state = 0, Auction
    PageResultsDTO<AuctionListDTO, AuctionList> getList1(PageRequestDTO pageRequestDTO, String user);

    //목록처리2 경매 완료, 매칭 미완료 , test :state = 1, Auction
    PageResultsDTO<AuctionListDTO, AuctionList> getList2(PageRequestDTO pageRequestDTO, String user);

    //목록처리3 매칭완료, 일 수행 전 값   state=2(매칭 완료), ischosen=1(낙찰값) , auctionList-Bidding 정보 출력
    PageResultsDTO<AuctionBiddingDTO, Object[]> getList3(PageRequestDTO pageRequestDTO, String user);

    //목록처리4 매칭완료, 일 수행 후   state=3(일 수행완료), ischosen=1(낙찰값) , auctionList-Bidding 정보 출력
    PageResultsDTO<AuctionBiddingDTO, Object[]> getList4(PageRequestDTO pageRequestDTO, String user);

    //일 수행 완료 후 state값 변경
    void jobDone(Long aucSeq);

    //================================== helper =========================================//

    //헬퍼 참여한 경매 목록
    PageResultsDTO<AuctionBiddingDTO, Object[]> getMyBids(PageRequestDTO pageRequestDTO, String helper, boolean onAuction);

    //헬퍼 낙찰 내역
    PageResultsDTO<AuctionBiddingDTO, Object[]> getMyChosenBids(PageRequestDTO pageRequestDTO, String helper, boolean isAllDone);

    //경매 참여
    Long biddingIn(BiddingListDTO dto);

    //경매참여내역하나만 받아오기
    Optional<BiddingList> getOneBid(Long bidSeq);

    //경매 삭제 - helper가 경매 삭제 불가
    //void deleteBidding(Long bidSeq);

    //비딩 참여 가능한 옥션리스트 test :state = 0, Auction
    PageResultsDTO<AuctionListDTO, AuctionList> getAvailableAuctions(String sido, String sigungu, int category, PageRequestDTO pageRequestDTO);

    //================================== blog =========================================//
    //헬퍼 기준 일 수행 완료 된 갑 불러오기 state=3, auction-bidding <List>
    List<AuctionBiddingDTO> getAllWithState4ForHelper(String helper);

    //================================== DTO <-> Entity =========================================//

    //**dtoToEntity : 입력 받은 값을 엔티티로 변환 후 DB전달 **//*
    //AuctionList
    default AuctionList dtoToEntity(AuctionListDTO dto, Member user) {
        AuctionList auctionList = AuctionList.builder().auctionGap(dto.getAuctionGap()).aucSeq(dto.getAucSeq()).age(dto.getAge()).category(dto.getCategory())
                .content(dto.getContent()).user(user).driverLicense(dto.getDriverLicense()).doDateTime(dto.getDoDateTime()).regDate(dto.getRegDate())
                .gender(dto.getGender()).level(dto.getLevel()).predictHour(dto.getPredictHour()).startPrice(dto.getStartPrice()).title(dto.getTitle())
                .state(dto.getState()).address(dto.getAddress()).sido(dto.getSido()).sigungu(dto.getSigungu()).build();
        return auctionList;
    }

    //BiddingList
    default BiddingList dtoToEntity(AuctionList auctionList,BiddingListDTO dto, Member helper){
        BiddingList biddingList = BiddingList.builder().bidSeq(dto.getBidSeq()).aucSeq(auctionList).helper(helper).chosen(dto.getChosen())
                .offerPrice(dto.getOfferPrice()).build();
        return biddingList;
    }

    /** entityToDto DB에서 가져 온 값을 dto 전환 후 출력 **/
    //AuctionList
    default AuctionListDTO entityToDTO(AuctionList auc){

        AuctionListDTO auctionListDTO = AuctionListDTO.builder().auctionGap(auc.getAuctionGap()).age(auc.getAge()).aucSeq(auc.getAucSeq()).user(auc.getUser().getId())
                .category(auc.getCategory()).content(auc.getContent()).doDateTime(auc.getDoDateTime()).regDate(auc.getRegDate()).driverLicense(auc.getDriverLicense())
                .gender(auc.getGender()).level(auc.getLevel()).predictHour(auc.getPredictHour()).startPrice(auc.getStartPrice()).state(auc.getState())
                .address(auc.getAddress()).sido(auc.getSido()).sigungu(auc.getSigungu())
                .title(auc.getTitle()).aucSeq(auc.getAucSeq()).build();
        return auctionListDTO;
    }

    //AuctionList,User
    default AuctionListDTO entityToDTO(AuctionList auc, Member user){
        AuctionListDTO auctionListDTO = AuctionListDTO.builder().auctionGap(auc.getAuctionGap()).age(auc.getAge()).aucSeq(auc.getAucSeq())
                .category(auc.getCategory()).content(auc.getContent()).doDateTime(auc.getDoDateTime()).regDate(auc.getRegDate()).driverLicense(auc.getDriverLicense())
                .gender(auc.getGender()).user(user.getId()).level(auc.getLevel()).predictHour(auc.getPredictHour()).startPrice(auc.getStartPrice()).state(auc.getState())
                .address(auc.getAddress()).sido(auc.getSido()).sigungu(auc.getSigungu()).userNickName(user.getNickname()).userPhoto(user.getPhoto())
                .title(auc.getTitle()).aucSeq(auc.getAucSeq()).build();
        return auctionListDTO;
    }


    //AuctionList,BiddingList,helper
    default AuctionBiddingDTO entityToDTO(AuctionList auc, BiddingList bid, Member helper){
        AuctionBiddingDTO DTO = AuctionBiddingDTO.builder().auctionGap(auc.getAuctionGap()).age(auc.getAge()).aucSeq(auc.getAucSeq()).user(auc.getUser().getId())
                .category(auc.getCategory()).content(auc.getContent()).doDateTime(auc.getDoDateTime()).regDate(auc.getRegDate()).driverLicense(auc.getDriverLicense())
                .gender(auc.getGender()).level(auc.getLevel()).predictHour(auc.getPredictHour()).startPrice(auc.getStartPrice()).state(auc.getState())
                .title(auc.getTitle()).aucSeq(auc.getAucSeq()).address(auc.getAddress()).sido(auc.getSido()).sigungu(auc.getSigungu()).helperNickName(helper.getNickname())
                .userPhoto(auc.getUser().getPhoto()).helperPhoto(helper.getPhoto())
                .chosen(bid.getChosen()).bidSeq(bid.getBidSeq()).helper(helper.getId()).offerPrice(bid.getOfferPrice()).build();
        return DTO;
    }

    //AuctionList,user,BiddingList,helper
    default AuctionBiddingDTO entityToDTO(AuctionList auc, Member user, BiddingList bid, Member helper){
        AuctionBiddingDTO DTO = AuctionBiddingDTO.builder().auctionGap(auc.getAuctionGap()).age(auc.getAge()).aucSeq(auc.getAucSeq()).user(user.getId())
                .category(auc.getCategory()).content(auc.getContent()).doDateTime(auc.getDoDateTime()).regDate(auc.getRegDate()).driverLicense(auc.getDriverLicense())
                .gender(auc.getGender()).level(auc.getLevel()).predictHour(auc.getPredictHour()).startPrice(auc.getStartPrice()).state(auc.getState())
                .title(auc.getTitle()).aucSeq(auc.getAucSeq()).address(auc.getAddress()).sido(auc.getSido()).sigungu(auc.getSigungu()).helperNickName(helper.getNickname())
                .userPhoto(user.getPhoto()).helper(helper.getPhoto())
                .userNickName(user.getName()).chosen(bid.getChosen()).bidSeq(bid.getBidSeq()).helper(helper.getId()).offerPrice(bid.getOfferPrice()).build();
        return DTO;
    }

    //AuctionList,BiddingList
    default AuctionBiddingDTO entityToDTO(AuctionList auc,BiddingList bid){
        AuctionBiddingDTO DTO = AuctionBiddingDTO.builder().auctionGap(auc.getAuctionGap()).age(auc.getAge()).aucSeq(auc.getAucSeq())
                .category(auc.getCategory()).content(auc.getContent()).doDateTime(auc.getDoDateTime()).regDate(auc.getRegDate()).driverLicense(auc.getDriverLicense())
                .gender(auc.getGender()).level(auc.getLevel()).predictHour(auc.getPredictHour()).startPrice(auc.getStartPrice()).state(auc.getState())
                .title(auc.getTitle()).aucSeq(auc.getAucSeq()).address(auc.getAddress()).sido(auc.getSido()).sigungu(auc.getSigungu())
                .userNickName(auc.getUser().getNickname()).userPhoto(auc.getUser().getPhoto()).helperPhoto(bid.getHelper().getPhoto()).helperNickName(bid.getHelper().getNickname())
                .chosen(bid.getChosen()).bidSeq(bid.getBidSeq()).helper(bid.getHelper().getId()).offerPrice(bid.getOfferPrice()).build();
        return DTO;
    }

    //BiddingList
    default BiddingListDTO entityToDTO(BiddingList bid){
        BiddingListDTO biddingListDTO = BiddingListDTO.builder().aucSeq(bid.getAucSeq().getAucSeq()).helper(bid.getHelper().getId()).bidSeq(bid.getBidSeq()).chosen(bid.getChosen())
                .offerPrice(bid.getOfferPrice()).helperNickName(bid.getHelper().getNickname()).helperPhoto(bid.getHelper().getPhoto()).build();
        return biddingListDTO;
    }
}
