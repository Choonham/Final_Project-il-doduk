package com.finalproject.ildoduk.entity.chat;

import com.finalproject.ildoduk.entity.BaseEntity;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Chat   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long No;
    private String message;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member reciver;
    private String sender;
    private String time;
    @ManyToOne(fetch = FetchType.LAZY)
    private AuctionList list;
    


}
