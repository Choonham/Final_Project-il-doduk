package com.finalproject.ildoduk.service.member.service;

import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MemberService {

    public List<String> getList();

    void userRegister(MemberDto dto);

    MemberDto userIdCheck(String id);

    MemberDto userIdDtoInit(MemberDto dto);

    MemberDto userIdPwdCheck(String id, String pwd);

    int nickNameCheck(String nickname); //유저 닉네임 db확인

    MemberDto userNickCheck(String nick);  //해당 닉네임에 관련된 정보 가져오기

    void userModify(MemberDto dto);

    void userDelete(String id);

    MemberDto userToHelperIdCheck(String memberId);



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
                .sido(dto.getSido())
                .sigungu(dto.getSigungu())
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
                .sido(entity.getSido())
                .sigungu(entity.getSigungu())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .intro(entity.getIntro())
                .state(entity.getState())
                .regDate(entity.getRegDate())
                .build();

        return dto;
    }


}
