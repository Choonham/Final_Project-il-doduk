package com.finalproject.ildoduk.service.bidding.service;

import com.finalproject.ildoduk.dto.auction.BiddingListDTO;
import com.finalproject.ildoduk.entity.auction.BiddingList;

public interface BiddingService {

    BiddingListDTO get_bidding(String no);



    default BiddingListDTO entityToDTO(BiddingList bid){
        BiddingListDTO biddingListDTO = BiddingListDTO.builder().aucSeq(bid.getAucSeq().getAucSeq()).helper(bid.getHelper().getId()).bidSeq(bid.getBidSeq()).chosen(bid.getChosen())
                .offerPrice(bid.getOfferPrice()).helperNickName(bid.getHelper().getNickname()).helperPhoto(bid.getHelper().getPhoto()).build();
        return biddingListDTO;
    }


}