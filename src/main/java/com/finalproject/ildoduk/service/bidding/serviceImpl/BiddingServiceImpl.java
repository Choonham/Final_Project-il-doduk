package com.finalproject.ildoduk.service.bidding.serviceImpl;

import com.finalproject.ildoduk.dto.review.BidDTO;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.repository.auction.BiddingListRepository;
import com.finalproject.ildoduk.service.bidding.service.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BiddingServiceImpl implements BiddingService {
    @Autowired
    BiddingListRepository repo;
/*널 체크 아직 안하긴 했는데*/
    @Override
    public BidDTO get_bidding(String no) {

    Long bid_no = Long.parseLong(no);


        return Bid_EntityToDto(repo.findById(bid_no).get());
    }







}
