package com.finalproject.ildoduk.entity.pay;

import com.finalproject.ildoduk.entity.BaseEntity;
import com.finalproject.ildoduk.entity.auction.AuctionList;
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

    @ManyToOne
    private Member id;

    @ManyToOne
    private Member userId;

    @ManyToOne
    private AuctionList aucNo;

    private String aucTitle;
    private String aucContent;
    private int aucPrice;
    private int aucState;

}
