package com.finalproject.ildoduk.service.member.service;

import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MemberService {

   List<String> getList();

    void userRegister(MemberDto dto);

    MemberDto userIdCheck(String id);

    int nickNameCheck(String nickname); //유저 닉네임 db확인

    MemberDto userNickCheck(String nick);  //해당 닉네임에 관련된 정보 가져오기

    void userModify(MemberDto dto);

    void userDelete(String id);


    //헬퍼 승인 시에 state 2로 변경
    void updateState(MemberDto memberDto);

//-------------------- 유저 포인트 관련 ----------------------
    //결제 성공시에 현재 가지고 있는 포인트에서 증가
    void updatePoint(PaymentDTO dto);
    //환불 시에 현재 가지고 있는 포인트에서 차감
    void minusPoint(MemberDto dto);


    MemberDto kakaoLogin(@RequestBody String json) ;

    default Member dtoToEntity(MemberDto dto){

        Member entity = Member.builder()
                .id(dto.getId())
                .name(dto.getName())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .nickname(dto.getNickname())
                .sido(dto.getSido())
                .sigungu(dto.getSigungu())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .point(dto.getPoint())
                .photo(dto.getPhoto())
                .phone(dto.getPhone())
                .intro(dto.getIntro())
                .state(dto.getState())
                .photo(dto.getPhoto())
                .build();

        return entity;
    }

    default MemberDto EntityToDto(Member entity){

        MemberDto dto = MemberDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .gender(entity.getGender())
                .birth(entity.getBirth())
                .nickname(entity.getNickname())
                .sido(entity.getSido())
                .sigungu(entity.getSigungu())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .point(entity.getPoint())
                .photo(entity.getPhone())
                .intro(entity.getIntro())
                .state(entity.getState())
                .photo(entity.getPhoto())
                .regDate(entity.getRegDate())
                .build();

        return dto;
    }

    //경매 등록시에 포인트 차감(보증금 걸어놓는것처럼)
    void minusPonit(MemberDto dto);

    //경매 미매칭시에 다시 원래 금액 돌려줌
    void refundAuctionPay(MemberDto dto);
}
