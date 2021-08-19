package com.finalproject.ildoduk.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeHistoryDTO {
    //거래 이력에 필요한 거래 번호(pk)
    private Long tNo;
    private Long aucNo;
    private String id;       // 현재 접속 계정
    private String userId;   // 거래한 상대방 계정

    private String aucTitle; // 경매 제목
    private String aucContent; //경매 내용

    private int aucPrice;     // 경매 가격
    private int aucState;    // 경매 상황

    private LocalDateTime regDate;

}
