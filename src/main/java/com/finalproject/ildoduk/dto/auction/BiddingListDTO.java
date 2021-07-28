package com.finalproject.ildoduk.dto.auction;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BiddingListDTO {

    private Long bidSeq;

    private String helper;
    private Long aucSeq;

    private int chosen;
    private int offerPrice;
}
