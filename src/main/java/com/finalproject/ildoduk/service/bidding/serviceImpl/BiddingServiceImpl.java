package com.finalproject.ildoduk.service.bidding.serviceImpl;

import com.finalproject.ildoduk.dto.auction.BiddingListDTO;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.repository.auction.BiddingListRepository;
import com.finalproject.ildoduk.service.bidding.service.BiddingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BiddingServiceImpl implements BiddingService {
    @Autowired
    BiddingListRepository repo;

    /*널 체크 아직 안하긴 했는데*/
    @Override
    public BiddingListDTO get_bidding(String no) {

    Long bid_no = Long.parseLong(no);
    Optional<BiddingList> result =repo.findById(bid_no);
        BiddingListDTO dto=entityToDTO(result.get());
     return dto;

    }







}
