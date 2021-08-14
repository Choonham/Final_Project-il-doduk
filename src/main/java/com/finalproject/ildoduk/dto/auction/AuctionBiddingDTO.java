package com.finalproject.ildoduk.dto.auction;


import lombok.*;

import java.time.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuctionBiddingDTO {

    //기본키
    private Long aucSeq;

    //Member(id)외래키
    private String user;
    private String userNickName;
    private String userPhoto;

    private String address;
    private String sido;
    private String sigungu;

    //JOIN으로 처리할 Bidding 테이블 정보
    private Long bidSeq;
    private String helper;  //Member(id) 외래키
    private String helperNickName;
    private String helperName;
    private int chosen;
    private int offerPrice;
    private String helperPhoto;

    private int category;
    private LocalDateTime doDateTime;
    private String predictHour;
    private int level;
    private int startPrice;
    private int auctionGap;
    private String title;
    private String content;
    private LocalDateTime regDate;
    private int driverLicense;
    private int age;
    private int gender;
    private int state;

   // private String image;
}
