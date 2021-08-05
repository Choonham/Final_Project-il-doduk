package com.finalproject.ildoduk.entity.pay;

import com.finalproject.ildoduk.entity.BaseEntity;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@Table(name = "trade_history_tbl")
@NoArgsConstructor
@DynamicInsert
public class TradeHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tNo;

    // 접속 계정 정보
    @ManyToOne
    private Member id;

    //입찰 정보 ( 가격과 헬퍼 정보 )
    @ManyToOne
    private BiddingList bidSeq;
    // 경매 내역
    @ManyToOne
    private AuctionList aucNo;


}
