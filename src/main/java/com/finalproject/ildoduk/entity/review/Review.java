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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long no;

    //비딩 리스트에 포함된 친구들
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="bidSeq" )
    private BiddingList bidSeq;


    //글쓴이 부분
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="id")
    private Member id;

    private String title;

    @Column(length = 10000, nullable = false)
    private String content;


}
