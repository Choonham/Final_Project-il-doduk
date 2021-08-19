package com.finalproject.ildoduk.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelperInfoDTO {
    private long helperNo;
    private String memberId;
    private String goodAtFirst;
    private String goodAtSecond;
    private String goodAtThird;
    private int kindness;
    private String appeal;
    private String img;
    private String photo;
    private int agreeHelper;
    private String helperNick;
    private LocalDateTime regDate;  //헬퍼 신청일
}
