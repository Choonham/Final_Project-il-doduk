package com.finalproject.ildoduk.service.serviceCenter.service;


import com.finalproject.ildoduk.dto.serviceCenter.CustomerAnswerDTO;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.serviceCenter.CustomerAnswer;
import com.finalproject.ildoduk.entity.serviceCenter.CustomerBoard;

public interface CustomerAnswerService {

    //답글남기기
    void insertBoard(CustomerAnswerDTO dto);
    //답글 목록
    CustomerAnswerDTO getAnswer(Long cusNo);
    Long deleteComment(Long aNo);


    default CustomerAnswer dtoToEntity(CustomerAnswerDTO dto){

        Member member = Member.builder().id(dto.getAWriter()).build();
        CustomerBoard customerBoard = CustomerBoard.builder().cusNo(dto.getCusNo()).build();

        CustomerAnswer entity = CustomerAnswer.builder()
                .aNo(dto.getANo())
                .cusNo(customerBoard)
                .aContent(dto.getAContent())
                .aWriter(member)
                .build();

        return entity;
    }

    default CustomerAnswerDTO entityToDto(CustomerAnswer entity){

        CustomerAnswerDTO dto = CustomerAnswerDTO.builder()
                .aNo(entity.getANo())
                .cusNo(entity.getCusNo().getCusNo())
                .aContent(entity.getAContent())
                .aWriter(entity.getAWriter().getId())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }
}
