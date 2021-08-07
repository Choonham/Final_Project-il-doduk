package com.finalproject.ildoduk.service.member.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.dto.serviceCenter.CustomerBoardDTO;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.serviceCenter.CustomerBoard;

import java.util.Optional;

public interface HelperInfoService {

    //헬퍼 회원가입
    void helperRegister(HelperInfoDTO helperInfoDTO);

    //헬퍼 회원가입전 DB에 아이디 중복 체크
    int helperRegisterIdCheck(String memberId);

    //헬퍼 아이디체크 후 모든정보 반환(Member, HelperInfo)
    HelperInfoDTO helperFindById(String memberId);

    PageResultsDTO<MemberHelperInfoDTO, Object[]> getHelperInfoByLoc(String sigungu, PageRequestDTO requestDTO);
    int countHelpersBySigungu(String sigungu);

    //헬퍼 가입 승인을 위한 state 체크
    PageResultsDTO<HelperInfoDTO, HelperInfo> helperRequest(PageRequestDTO pageRequestDTO);
    //헬퍼 가입 승인
    void accept(HelperInfoDTO helperInfoDTO);
    //헬퍼 가입 반려
    void deny(HelperInfoDTO helperInfoDTO);
    //헬퍼 정보
    HelperInfoDTO helperInfo(HelperInfoDTO helperInfoDTO);



    default HelperInfo dtoToEntity(HelperInfoDTO dto){
        Member member = Member.builder().id(dto.getMemberId()).build();

        HelperInfo entity = HelperInfo.builder()
                .helperNo(dto.getHelperNo())
                .memberId(member)
                .goodAtFirst(dto.getGoodAtFirst())
                .goodAtSecond(dto.getGoodAtSecond())
                .goodAtThird(dto.getGoodAtThird())
                .kindness(dto.getKindness())
                .appeal(dto.getAppeal())
                .agreeHelper(dto.getAgreeHelper())
                .build();
        return entity;
    }

    default HelperInfoDTO EntityToDTO(HelperInfo entity){
        HelperInfo member = HelperInfo.builder().memberId(entity.getMemberId()).build();

        HelperInfoDTO dto = HelperInfoDTO.builder()
                .helperNo(entity.getHelperNo())
                .memberId(member.getMemberId().getId())
                .goodAtFirst(entity.getGoodAtFirst())
                .goodAtSecond(entity.getGoodAtSecond())
                .goodAtThird(entity.getGoodAtThird())
                .kindness(entity.getKindness())
                .appeal(entity.getAppeal())
                .agreeHelper(entity.getAgreeHelper())
                .build();
        return dto;
    }

    /**
    default MemberHelperInfoDTO entityToDTO(HelperInfo entity){

        MemberHelperInfoDTO dto = MemberHelperInfoDTO.builder()
                .helperNo(entity.getHelperNo())
                .memberId(entity.getMemberId().getId())
                .goodAtFirst(entity.getGoodAtFirst())
                .goodAtSecond(entity.getGoodAtSecond())
                .goodAtThird(entity.getGoodAtThird())
                .kindness(entity.getKindness())
                .appeal(entity.getAppeal())
                .build();

        return dto;
    }
     **/

    default MemberHelperInfoDTO entityToDTO(HelperInfo helper, Member member){
/*
        MemberDto memberDto = MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .address(member.getAddress())
                .intro(member.getIntro())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .photo(member.getPhoto())
                .build();

        HelperInfoDTO helperInfoDTO = HelperInfoDTO.builder()
                .appeal(helper.getAppeal())
                .helperNo(helper.getHelperNo())
                .kindness(helper.getKindness())
                .goodAtThird(helper.getGoodAtThird())
                .goodAtSecond(helper.getGoodAtSecond())
                .goodAtFirst(helper.getGoodAtFirst())
                .agreeHelper(helper.getAgreeHelper())
                .build();*/

        MemberHelperInfoDTO dto = MemberHelperInfoDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .address(member.getAddress())
                .intro(member.getIntro())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .photo(member.getPhoto())

                .appeal(helper.getAppeal())
                .helperNo(helper.getHelperNo())
                .kindness(helper.getKindness())
                .goodAtThird(helper.getGoodAtThird())
                .goodAtSecond(helper.getGoodAtSecond())
                .goodAtFirst(helper.getGoodAtFirst())
                .agreeHelper(helper.getAgreeHelper())
                .build();

        return dto;
    }
}
