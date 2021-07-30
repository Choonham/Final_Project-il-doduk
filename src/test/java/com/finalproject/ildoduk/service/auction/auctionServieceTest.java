package com.finalproject.ildoduk.service.auction;

import com.finalproject.ildoduk.dto.*;
import com.finalproject.ildoduk.dto.auction.*;
import com.finalproject.ildoduk.service.auction.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

@SpringBootTest
public class auctionServieceTest {

    @Autowired
    private AuctionService auctionService;

    @Test
    public void testGetMyBids(){
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResultsDTO<AuctionBiddingDTO, Object[]> result = auctionService.getMyBids(pageRequestDTO,"user5");
        System.out.println(result.getDtoList().size());
        for(AuctionBiddingDTO DTO: result.getDtoList()){
            System.out.println(DTO);
        }
    }

}
