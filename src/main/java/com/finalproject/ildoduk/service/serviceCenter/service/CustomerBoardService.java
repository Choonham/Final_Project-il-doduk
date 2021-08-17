package com.finalproject.ildoduk.service.serviceCenter.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.serviceCenter.CustomerBoardDTO;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.serviceCenter.CustomerBoard;


public interface CustomerBoardService {

    //전체 게시글 조회
    PageResultsDTO<CustomerBoardDTO,CustomerBoard> allContents(PageRequestDTO pageRequestDTO);
    //문의글 작성
    void insertCusBoard(CustomerBoardDTO dto);
    //글 상세 보기
    CustomerBoardDTO getBoardList(Long cusNo);

    void updateBoard(CustomerBoardDTO dto);

    void deleteBoard(CustomerBoardDTO dto);
    void updateAnswer(CustomerBoardDTO dto);

    default CustomerBoard dtoToEntity(CustomerBoardDTO dto){

        Member member = Member.builder().id(dto.getCusWriter()).build();

        CustomerBoard entity = CustomerBoard.builder()
                .cusNo(dto.getCusNo())
                .cusTitle(dto.getCusTitle())
                .cusContent(dto.getCusContent())
                .cusWriter(member)
                .secretBoard(dto.getSecretBoard())
                .passwordBoard(dto.getPasswordBoard())
                .answerCheck(dto.getAnswerCheck())
                .build();

        return entity;
    }

    default CustomerBoardDTO entityToDTO(CustomerBoard entity){

        CustomerBoardDTO dto = CustomerBoardDTO.builder()
                .cusNo(entity.getCusNo())
                .cusTitle(entity.getCusTitle())
                .cusContent(entity.getCusContent())
                .cusWriter(entity.getCusWriter().getId())
                .secretBoard(entity.getSecretBoard())
                .passwordBoard(entity.getPasswordBoard())
                .answerCheck(entity.getAnswerCheck())
                .cusNickName(entity.getCusWriter().getNickname())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

}
