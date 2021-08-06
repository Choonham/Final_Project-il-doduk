package com.finalproject.ildoduk.dto.review;

import com.finalproject.ildoduk.dto.auction.AuctionListDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import lombok.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class BidDTO {

    private Long bidSeq;

    private MemberDto helper;

    private AuctionDTO aucSeq;

    private int chosen;
    private int offerPrice;

}
