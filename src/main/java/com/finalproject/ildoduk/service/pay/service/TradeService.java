package com.finalproject.ildoduk.service.pay.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.pay.TradeHistoryDTO;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.pay.TradeHistory;

public interface TradeService {
    //전체 게시글 조회
    PageResultsDTO<TradeHistoryDTO, TradeHistory> allContents(TradeHistoryDTO tradeHistoryDTO, PageRequestDTO pageRequestDTO);


    //DTO -> Entity
    default TradeHistory dtoToEntity(TradeHistoryDTO dto){
        Member id = Member.builder().id(dto.getId()).build();
        Member userId = Member.builder().id(dto.getUserId()).build();
        AuctionList tradeHistory = AuctionList.builder().aucSeq(dto.getAucNo()).build();

        TradeHistory entity = TradeHistory.builder()
                .tNo(dto.getTNo())
                .aucNo(tradeHistory)
                .id(id)
                .userId(userId)
                .aucTitle(dto.getAucTitle())
                .aucContent(dto.getAucContent())
                .aucPrice(dto.getAucPrice())
                .aucState(dto.getAucState())
                .build();

        return entity;
    }

    //반대로 Entity -> DTO로 변환
    default TradeHistoryDTO entityToDto(TradeHistory entity){
        TradeHistoryDTO dto = TradeHistoryDTO.builder()
                .tNo(entity.getTNo())
                .aucNo(entity.getAucNo().getAucSeq())
                .id(entity.getId().getId())
                .userId(entity.getUserId().getId())
                .aucTitle(entity.getAucTitle())
                .aucContent(entity.getAucContent())
                .aucPrice(entity.getAucPrice())
                .aucState(entity.getAucState())
                .regDate(entity.getRegDate())
                .build();
        return dto;
    }

}
