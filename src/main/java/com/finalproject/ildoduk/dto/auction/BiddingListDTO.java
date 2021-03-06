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
    private String helperNickName;
    private String helperName;
    private String helperPhoto;

    private Long aucSeq;

    private int chosen;
    private int offerPrice;
}
