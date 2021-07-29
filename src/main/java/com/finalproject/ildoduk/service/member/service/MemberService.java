package com.finalproject.ildoduk.service.member.service;

import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.entity.member.Member;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MemberService {
    public List<String> getList();

    void userRegister(MemberDto dto);

    MemberDto userIdCheck(String id);

    MemberDto userIdDtoInit(MemberDto dto);

    MemberDto userIdPwdCheck(String id, String pwd);

    void userModify(MemberDto dto);

    void userDelete(String id);

// 유저 포인트 관련

    void updatePoint(PaymentDTO dto);
    void minusPonit(MemberDto dto);
    void plusPoint(MemberDto dto);


    MemberDto kakaoLogin(@RequestBody String json) ;

    default Member dtoToEntity(MemberDto dto){

        Member entity = Member.builder()
                .id(dto.getId())
                .pwd(dto.getPwd())
                .name(dto.getName())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .nickname(dto.getNickname())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .point(dto.getPoint())
                .photo(dto.getPhoto())
                .intro(dto.getIntro())
                .state(dto.getState())
                .build();

        return entity;
    }

    default MemberDto EntityToDto(Member entity){

        MemberDto dto = MemberDto.builder()
                .id(entity.getId())
                .pwd(entity.getPwd())
                .name(entity.getName())
                .gender(entity.getGender())
                .birth(entity.getBirth())
                .nickname(entity.getNickname())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .intro(entity.getIntro())
                .state(entity.getState())
                .build();

        return dto;
    }



}
