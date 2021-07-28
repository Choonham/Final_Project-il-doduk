package com.finalproject.ildoduk.service.pay;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
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
