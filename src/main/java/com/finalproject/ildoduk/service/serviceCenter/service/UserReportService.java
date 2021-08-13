package com.finalproject.ildoduk.service.serviceCenter.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.pay.TradeHistoryDTO;
import com.finalproject.ildoduk.dto.serviceCenter.UserReportDTO;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;

import java.util.Optional;

public interface UserReportService {

    void insertReport(UserReportDTO userReportDTO);

    //신고 내역 조회
    PageResultsDTO<UserReportDTO, UserReport>  getReportList(UserReportDTO userReportDTO,PageRequestDTO pageRequestDTO);

    //신고 글 상세보기
    UserReportDTO badUserReportDetail(UserReportDTO userReportDTO);

    //신고 삭제
    void reportDelete(UserReportDTO userReportDTO);

    //관리자 : 신고 게시판 전체 조회
    //reportState == 1
    PageResultsDTO<UserReportDTO, UserReport> getStateOne(PageRequestDTO pageRequestDTO);
    //reportState == 2
    PageResultsDTO<UserReportDTO, UserReport> getStateTwo(PageRequestDTO pageRequestDTO);

    //신고 처리
    void updateReportState(UserReportDTO userReportDTO);

    //신고에 따른 친절점수 마이너스
    void minusKindness(UserReportDTO userReportDTO);


    default UserReport dtoToEntity(UserReportDTO dto){

        Member id = Member.builder().id(dto.getId()).build();
        Member helper = Member.builder().id(dto.getReportTarget()).build();

        HelperInfo reportTarget = HelperInfo.builder().memberId(helper).build();

        UserReport entity = UserReport.builder()
                .reportNo(dto.getReportNo())
                .id(id)
                .reportTarget(helper)
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
                .targetNickName(entity.getReportTarget().getNickname())
                .build();
        return dto;
    }

    //유저 목록
    default MemberDto EntityToDto(Member entity){

        MemberDto dto = MemberDto.builder()
                .id(entity.getId())
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


}
