package com.finalproject.ildoduk.dto.auction;

import lombok.*;

import java.time.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuctionListDTO {
    private Long aucSeq;
    private Long timer;

    //User(id)외래키
    private String user;
    private String userNickName;
    private String userPhoto;

    private String address;
    private String sido;
    private String sigungu;

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

    //private String image;
}