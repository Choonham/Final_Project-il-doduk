package com.finalproject.ildoduk.service.bidding.service;

import com.finalproject.ildoduk.dto.auction.AuctionListDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.review.AuctionDTO;
import com.finalproject.ildoduk.dto.review.BidDTO;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.entity.member.Member;

public interface BiddingService {

    BidDTO get_bidding (String no);




    default MemberDto Mem_EntityToDto(Member entity){

        MemberDto dto = MemberDto.builder()
                .id(entity.getId())
                .pwd(entity.getPwd())
                .name(entity.getName())
                .gender(entity.getGender())
                .birth(entity.getBirth())
                .nickname(entity.getNickname())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .intro(entity.getIntro())
                .state(entity.getState())
                .build();

        return dto;
    }


    default BidDTO Bid_EntityToDto(BiddingList bid){



    BidDTO dto = BidDTO.builder().aucSeq(auc_entityToDTO(bid.getAucSeq())).chosen(bid.getChosen()).helper(Mem_EntityToDto(bid.getHelper())).offerPrice(bid.getOfferPrice()).build();

    return dto;
    }


    default AuctionDTO auc_entityToDTO(AuctionList auc){

        AuctionDTO auctionListDTO = AuctionDTO.builder().auctionGap(auc.getAuctionGap()).age(auc.getAge()).aucSeq(auc.getAucSeq()).user(Mem_EntityToDto(auc.getUser()))
                .category(auc.getCategory()).content(auc.getContent()).doDateTime(auc.getDoDateTime()).regDate(auc.getRegDate()).driverLicense(auc.getDriverLicense())
                .gender(auc.getGender()).level(auc.getLevel()).predictHour(auc.getPredictHour()).startPrice(auc.getStartPrice()).state(auc.getState())
                .address(auc.getAddress()).sido(auc.getSido()).sigungu(auc.getSigungu())
                .title(auc.getTitle()).aucSeq(auc.getAucSeq()).build();
        return auctionListDTO;
    }

}
