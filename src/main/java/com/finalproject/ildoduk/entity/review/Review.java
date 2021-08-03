package com.finalproject.ildoduk.entity.review;

import com.finalproject.ildoduk.entity.BaseEntity;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString

public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aucSeq")
    private AuctionList aucSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="id" )
    private Member client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="bidSeq" )
    private BiddingList biddingList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="helperNo")
    private HelperInfo helperInfo;

    private String title;

    private String content;


}
