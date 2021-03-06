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

import javax.servlet.http.HttpSession;
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
    public @ResponseBody void pointCharge(@RequestParam("userId") Member member, HttpSession session, PaymentDTO dto){
        //멤버객체가 넘어온다. 아이디만 뽑아서 사용해야한다.
        log.info("결제 대상의 아이디  :  "+ member);
        log.info("결제 대상의 아이디  :  "+ member.getId());
        dto.setUserId(member.getId());
        paymentService.pointCharge(dto);
        memberService.updatePoint(dto);
        MemberDto memberDto = memberService.userIdCheck(member.getId());

        session.removeAttribute("user");
        session.setAttribute("user", memberDto);
    }


}
