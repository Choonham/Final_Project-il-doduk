package com.finalproject.ildoduk.service.member.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;

public interface HelperInfoService {

    HelperInfoDTO helperFindById(String memberId);

    PageResultsDTO<MemberHelperInfoDTO, Object[]> getHelperInfoByLoc(String sigungu, PageRequestDTO requestDTO);
    int countHelpersBySigungu(String sigungu);

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
                .build();
        return dto;
    }

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

    default MemberHelperInfoDTO entityToDTO(HelperInfo helper, Member member){

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
                .build();

        MemberHelperInfoDTO dto = MemberHelperInfoDTO.builder()
                .helperInfoDTO(helperInfoDTO)
                .memberDto(memberDto)
                .build();

        return dto;
    }
}
