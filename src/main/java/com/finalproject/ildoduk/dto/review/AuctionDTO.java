package com.finalproject.ildoduk.dto.review;

import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
public class AuctionDTO {



    private Long aucSeq;


    private MemberDto user;


    private int category;


    private String address;


    private String sido;


    private String sigungu;

    private LocalDateTime doDateTime;


    private String predictHour;


    private int level;


    private int startPrice;


    private int auctionGap;


    private String title;


    private String content;


    private LocalDateTime regDate;


    private int driverLicense;

    private int gender;

    private int age;

    private int state;


}
