package com.finalproject.ildoduk.dto.chat;


import com.finalproject.ildoduk.entity.auction.AuctionList;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ChatAucDTO {
    private Long auc_sec;
    private String img;
    private String recive;
    private String message;
    private String date;
}
