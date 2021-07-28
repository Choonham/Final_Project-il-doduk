package com.finalproject.ildoduk.entity.auction;


import com.finalproject.ildoduk.entity.member.*;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"helper","aucSeq"})
@DynamicInsert
public class BiddingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    private AuctionList aucSeq;

    //@ManyToOne(fetch = FetchType.LAZY)
     //private Member helper;

    @Column(nullable = false)
    private int offerPrice;

    @ColumnDefault(value="0")
    private int chosen;

    public void changetChosen(int chosen){
        this.chosen = chosen;
    }

}
