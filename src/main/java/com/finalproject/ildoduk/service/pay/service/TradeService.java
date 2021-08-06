package com.finalproject.ildoduk.service.pay.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.pay.TradeHistoryDTO;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.pay.TradeHistory;

public interface TradeService {
    //전체 게시글 조회
    PageResultsDTO<TradeHistoryDTO, TradeHistory> allContents(TradeHistoryDTO tradeHistoryDTO, PageRequestDTO pageRequestDTO);


    //DTO -> Entity
    default TradeHistory dtoToEntity(TradeHistoryDTO dto){
        Member id = Member.builder().id(dto.getId()).build();
        Member helper = Member.builder().id(dto.getUserId()).build();

        AuctionList tradeHistory = AuctionList.builder().aucSeq(dto.getAucNo()).title(dto.getAucTitle())
                .content(dto.getAucContent()).build();

        BiddingList biddingList = BiddingList.builder().bidSeq(dto.getAucNo()).offerPrice(dto.getAucPrice()).helper(helper).build();

        TradeHistory entity = TradeHistory.builder()
                .tNo(dto.getTNo())
                .aucNo(tradeHistory)
                .id(id)
                .bidSeq(biddingList)
                .build();

        return entity;
    }

    //반대로 Entity -> DTO로 변환
    default TradeHistoryDTO entityToDto(TradeHistory entity){
        TradeHistoryDTO dto = TradeHistoryDTO.builder()
                .tNo(entity.getTNo())
                .aucNo(entity.getAucNo().getAucSeq())
                .id(entity.getId().getId())
                .userId(entity.getBidSeq().getHelper().getId())
                .aucTitle(entity.getAucNo().getTitle())
                .aucContent(entity.getAucNo().getContent())
                .aucPrice(entity.getBidSeq().getOfferPrice())
                .aucState(entity.getAucNo().getState())
                .regDate(entity.getRegDate())
                .build();
        return dto;
    }

}
