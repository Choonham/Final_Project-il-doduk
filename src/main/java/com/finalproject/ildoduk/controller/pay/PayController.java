package com.finalproject.ildoduk.controller.pay;


import com.finalproject.ildoduk.dto.auction.AuctionBiddingDTO;
import com.finalproject.ildoduk.dto.auction.AuctionListDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.service.auction.service.AuctionService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import com.finalproject.ildoduk.service.pay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RequestMapping("/kakaoPay")
@Controller
@RequiredArgsConstructor
@Log4j2
public class PayController {

    private final PaymentService paymentService;
    private final MemberService memberService;
    private final AuctionService auctionService;

    //카카오 페이지 이동시에 조회한 사용자 데이터를 가지고 전환됩니다.
    @GetMapping("/kakao")
    public void postKakao(){
        log.info("post 실행됨");
    }


    //결제 성공시,
    @GetMapping("/success")
    public @ResponseBody void pointCharge(@RequestParam("userId") Member member, PaymentDTO dto){
        //멤버객체가 넘어온다. 아이디만 뽑아서 사용해야한다.
        log.info("결제 대상의 아이디  :  "+ member);
        log.info("결제 대상의 아이디  :  "+ member.getId());
        dto.setUserId(member.getId());
        paymentService.pointCharge(dto);

        //결제 목록을 업데이트 한 후에 사용자의 캐쉬를 현재 충전한 금액으로 다시 추가합니다.
        //마찬가지로 기존 위에서 사용한 dto 데이터를 넘겨준다 ( 계정, 결제금액 )
        memberService.updatePoint(dto);
    }


    //거래 시 해당 가격에 맞게 포인트 차감
    @PostMapping("/trade")
    public String trade(@RequestParam(value = "userID") String userID,
                        @RequestParam(value = "pay") String pay,
                        @RequestParam(value = "helper") String helperID , MemberDto dto){

        int point = Integer.parseInt(pay);

        dto.setId(userID);
        dto.setPoint(point);
        //사용자 포인트 차감감
        memberService.minusPonit(dto);

        //helper 정보를 조회하기 위해 다시 초기화
        MemberDto dtoOfHelper = new MemberDto();

        dtoOfHelper.setId(helperID);
        dtoOfHelper.setPoint(point);
        //헬퍼 포인트 증가
        memberService.plusPoint(dtoOfHelper);

        return "redirect:/login";
    }


//---------- 경매 결제 관련 ---------------------

    // 경매글을 등록했을때 보증금 개념으로 등록한 금액이 빠져나간다.
    public void regAuction(AuctionBiddingDTO auctionBiddingDTO, MemberDto memberDto){
        //1. 유저가 경매를 올렸을 경우 금액 차감 => 멤버 pointMinus에 해당 금액 마이너스
        int offerPrice = auctionBiddingDTO.getOfferPrice();
        //경매 값과 그 작성자
        memberDto.setPoint(offerPrice);
        memberDto.setId(auctionBiddingDTO.getUser());

        memberService.minusPonit(memberDto);

    }
    // 경매가 취소 되었을 경우 다시 해당 금액 넣어야한다.
    public void refundAuctionPay(AuctionBiddingDTO auctionBiddingDTO, MemberDto memberDto){
        //1. 유저가 경매를 올렸을 경우 금액 차감 => 멤버 pointMinus에 해당 금액 마이너스
        int offerPrice = auctionBiddingDTO.getOfferPrice();
        //경매 값과 그 작성자
        memberDto.setPoint(offerPrice);
        memberDto.setId(auctionBiddingDTO.getUser());

        memberService.plusPoint(memberDto);
    }

    // 경매가 낙찰되었을 경우 헬퍼 정보가 넘어와야한다.

    //






}
