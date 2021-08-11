package com.finalproject.ildoduk.service.pay.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.auction.AuctionBiddingDTO;
import com.finalproject.ildoduk.dto.auction.AuctionListDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.pay.Payment;


public interface PaymentService {

    void pointCharge(PaymentDTO dto);
    //결제 이력 조회
    PageResultsDTO<PaymentDTO,Payment> getHistory(String result, PageRequestDTO pageRequestDTO);
    //활불하기 위한 정보
    PaymentDTO toRefund(Long pointNo);
    //결제 이력 수정
    void updatePayCheck(PaymentDTO dto);

    //---------- 경매 결제 관련 ---------------------

    // 경매글을 등록했을때 : 보증금 개념으로 등록한 금액이 빠져나간다.
    void regAuction(Long aucSeq);
    // 경매가 취소 되었을 경우
    void refundAuctionPay(Long aucSeq);
    // 경매가 낙찰되었을 경우
    void biddingSuccess(Long bidSeq);
    //  유저가 일 끝내기 버튼을 눌렀을 경우 경매 가격을 헬퍼에게 넣어줘야한다.
    //  이때 헬퍼의 점수에 따라 수수료 부과
    void doneAuction(Long aucSeq);

    //DTO -> Entity
    default Payment dtoToEntity(PaymentDTO dto){
       //아이디는 멤버 엔티티 참조....
        Member member = Member.builder().id(dto.getUserId()).build();

        Payment entity = Payment.builder()
                .pointNo(dto.getPointNo())
                .userId(member)
                .totalPoint(dto.getTotalPoint())
                .payCheck(dto.getPayCheck())
                .build();
        return entity;
    }
    //반대로 Entity -> DTO로 변환
    default PaymentDTO entityToDto(Payment entity){
        PaymentDTO dto = PaymentDTO.builder()
                .pointNo(entity.getPointNo())
                .userId(entity.getUserId().getId())
                .totalPoint(entity.getTotalPoint())
                .payCheck(entity.getPayCheck())
                .regDate((entity.getRegDate()))
                .build();
         return dto;
    }
}
