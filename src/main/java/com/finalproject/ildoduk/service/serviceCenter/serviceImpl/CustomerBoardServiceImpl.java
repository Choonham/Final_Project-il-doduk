package com.finalproject.ildoduk.service.serviceCenter.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.serviceCenter.CustomerBoardDTO;
import com.finalproject.ildoduk.entity.member.QMember;
import com.finalproject.ildoduk.entity.serviceCenter.CustomerAnswer;
import com.finalproject.ildoduk.entity.serviceCenter.CustomerBoard;
import com.finalproject.ildoduk.entity.serviceCenter.QCustomerBoard;
import com.finalproject.ildoduk.repository.serviceCenter.CustomerAnswerRepository;
import com.finalproject.ildoduk.repository.serviceCenter.CustomerBoardRepository;
import com.finalproject.ildoduk.service.serviceCenter.service.CustomerBoardService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomerBoardServiceImpl implements CustomerBoardService {

    private final CustomerBoardRepository customerBoardRepository;
    private final CustomerAnswerRepository customerAnswerRepository;

    //문의 게시판 조회(전체 데이터)
    @Override
    public PageResultsDTO<CustomerBoardDTO, CustomerBoard> allContents(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("cusNo").descending());

        BooleanBuilder booleanBuilder = getSearch(pageRequestDTO);

        Page<CustomerBoard> result = customerBoardRepository.findAll(booleanBuilder, pageable);
        log.info(result + "문의 게시판 페이지 처리 ~~~~~~~~~~~~~ ");
        Function<CustomerBoard,CustomerBoardDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultsDTO<>(result, fn);
    }

    //검색 조건
    private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO){
        String type = pageRequestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QCustomerBoard qCustomerBoard = QCustomerBoard.customerBoard;
        QMember qMember = QMember.member;

        String keyword = pageRequestDTO.getKeyword();

        BooleanExpression expression = qCustomerBoard.cusNo.gt(0L);
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0){
            return booleanBuilder;
        }

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t")){
            conditionBuilder.or(qCustomerBoard.cusTitle.contains(keyword));
        }
        //연관관계를 한 이후부터 에러..500
        if(type.contains("w")){
            conditionBuilder.or(qCustomerBoard.cusWriter.nickname.contains(keyword));
        }
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }

    //문의 등록
    @Override
    public void insertCusBoard(CustomerBoardDTO dto) {

        CustomerBoard entity = dtoToEntity(dto);

        customerBoardRepository.save(entity);

    }
    //글 상세보기
    @Override
    public CustomerBoardDTO getBoardList(Long cusNo) {
        Optional<CustomerBoard> result = customerBoardRepository.findById(cusNo);
        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    //게시글 수정
    @Override
    public void updateBoard(CustomerBoardDTO dto) {
        Optional<CustomerBoard> update = customerBoardRepository.findById(dto.getCusNo());

        if(update.isPresent()){
            CustomerBoard entity = update.get();

            entity.changeTitle(dto.getCusTitle());
            entity.changeContent(dto.getCusContent());

            customerBoardRepository.save(entity);
        }
    }
    //문의글 삭제
    @Override
    public void deleteBoard(CustomerBoardDTO dto) {
        //댓글을 먼저 삭제 한 후에 게시글 삭제
        Optional<CustomerAnswer> comment = customerAnswerRepository.findByCusNoCusNo(dto.getCusNo());

        if(comment.isPresent()) {
            CustomerAnswer comment_entity = comment.get();
            customerAnswerRepository.delete(comment_entity);
        }

        Optional<CustomerBoard> result = customerBoardRepository.findById(dto.getCusNo());
        log.info(result + "삭제 내부");
        if(result.isPresent()){
            CustomerBoard entity = result.get();
            customerBoardRepository.delete(entity);
        }
    }

    //답변 여부 체크
    @Override
    public void updateAnswer(CustomerBoardDTO dto) {
        Optional<CustomerBoard> result = customerBoardRepository.findById(dto.getCusNo());

        if(result.isPresent()){
            CustomerBoard entity = result.get();
            entity.changeAnswerCheck(dto.getAnswerCheck());
            customerBoardRepository.save(entity);
        }
    }
}
