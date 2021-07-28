package com.finalproject.ildoduk.service.serviceCenter;

import com.finalproject.ildoduk.dto.serviceCenter.UserReportDTO;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;

public interface UserReportService {

    void insertReport(UserReportDTO userReportDTO);

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
}
