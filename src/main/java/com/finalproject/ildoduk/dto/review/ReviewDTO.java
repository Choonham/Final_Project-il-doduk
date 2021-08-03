package com.finalproject.ildoduk.dto.review;

import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class ReviewDTO {


    private long no;
    private AuctionList aucSeq;
    private Member client;
    private String title;
    private String content;
    private BiddingList biddingList;
    private HelperInfo helperInfo;
    private LocalDateTime regDate, modDate;
}
