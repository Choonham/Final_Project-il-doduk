package com.finalproject.ildoduk.service.auction;

import com.finalproject.ildoduk.dto.*;
import com.finalproject.ildoduk.dto.auction.*;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.repository.auction.AuctionListRepository;
import com.finalproject.ildoduk.repository.auction.BiddingListRepository;
import com.finalproject.ildoduk.repository.member.MemberRepository;
import com.finalproject.ildoduk.service.auction.service.*;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import javax.jdo.annotations.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class auctionServieceTest {

    @Autowired
    private AuctionService auctionService;
@Autowired
    BiddingListRepository repository;

@Autowired
    MemberRepository mem_repo;
@Autowired
BiddingListRepository biddingListRepository;
@Autowired
    AuctionListRepository auc_repo;
    @Test
    public void testGetMyBids(){
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResultsDTO<AuctionBiddingDTO, Object[]> result = auctionService.getMyBids(pageRequestDTO,"godnjs729417@naver.com",true);
        System.out.println(result.getDtoList().size());

        for(AuctionBiddingDTO DTO: result.getDtoList()){
            System.out.println(DTO);
        }
    }



    @Test
    public void testRegister(){
        for(int i=0; i < 10; i++){
            AuctionListDTO dto = AuctionListDTO.builder().auctionGap(100).category(1).content("test Dummy "+i).doDateTime(LocalDateTime.of(2022,01,01,0,0) )
                    .user("vhdvhd0307@naver.com").level(1).predictHour("2").title("test "+i).startPrice(20000).address("testteesteetste").sido("일").sigungu("이").build();

            Long aucSeq = auctionService.register(dto);
        }
    }

    @Test
    public void testBiddingIn() {

        Optional<BiddingList> op =repository.findById(1L);
        op.get().getAucSeq().getAddress();


    }





}
