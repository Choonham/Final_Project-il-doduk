package com.finalproject.ildoduk.service.member.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.blog.BlogCommentDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.blog.BlogComment;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import org.springframework.data.domain.Page;

public interface HelperInfoService {

    PageResultsDTO<MemberHelperInfoDTO, Object[]> getHelperInfoByLoc(String sigungu, PageRequestDTO requestDTO);


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

    default HelperInfoDTO entityToDTO(HelperInfo entity){
        HelperInfoDTO dto = HelperInfoDTO.builder()
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
        MemberHelperInfoDTO dto = MemberHelperInfoDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .intro(member.getIntro())
                .appeal(helper.getAppeal())
                .gender(member.getGender())
                .kindness(helper.getKindness())
                .goodAtFirst(helper.getGoodAtFirst())
                .goodAtSecond(helper.getGoodAtSecond())
                .goodAtThird(helper.getGoodAtThird())
                .photo(member.getPhoto())
                .build();
        return dto;
    }
}
