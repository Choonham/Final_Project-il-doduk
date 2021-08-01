package com.finalproject.ildoduk.service.serviceCenter.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.pay.TradeHistoryDTO;
import com.finalproject.ildoduk.dto.serviceCenter.UserReportDTO;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.pay.TradeHistory;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;

public interface UserReportService {

    void insertReport(UserReportDTO userReportDTO);

    //신고를 위한 거래 목록 조회
    TradeHistoryDTO getUser(String id);

    //신고 내역 조회
    PageResultsDTO<UserReportDTO, UserReport>  getReportList(UserReportDTO userReportDTO,PageRequestDTO pageRequestDTO);

    //신고 글 상세보기
    UserReportDTO badUserReportDetail(UserReportDTO userReportDTO);

    //신고 삭제
    void reportDelete(UserReportDTO userReportDTO);



    default UserReport dtoToEntity(UserReportDTO dto){

        Member id = Member.builder().id(dto.getId()).build();
        Member reportTarget = Member.builder().id(dto.getReportTarget()).build();

        UserReport entity = UserReport.builder()
                .reportNo(dto.getReportNo())
                .id(id)
                .reportTarget(reportTarget)
                .reportTitle(dto.getReportTitle())
                .reportContent(dto.getReportContent())
                .reportKind(dto.getReportKind())
                .reportState(dto.getReportState())
                .build();

        return entity;
    }

    default UserReportDTO entityToDto(UserReport entity){

        UserReportDTO dto = UserReportDTO.builder()
                .reportNo(entity.getReportNo())
                .id(entity.getId().getId())
                .reportTarget(entity.getReportTarget().getId())
                .reportTitle(entity.getReportTitle())
                .reportContent(entity.getReportContent())
                .reportKind(entity.getReportKind())
                .reportState(entity.getReportState())
                .regDate(entity.getRegDate())
                .build();
        return dto;
    }

    //유저 목록
    default MemberDto EntityToDto(Member entity){

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
                .build();

        return dto;
    }

    //거래 내역
    default TradeHistoryDTO entityToDtoTrade(TradeHistory entity){
        TradeHistoryDTO dto = TradeHistoryDTO.builder()
                .tNo(entity.getTNo())
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
