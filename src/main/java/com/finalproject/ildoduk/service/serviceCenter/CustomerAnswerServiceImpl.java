package com.finalproject.ildoduk.service.serviceCenter;

import com.finalproject.ildoduk.dto.serviceCenter.CustomerAnswerDTO;
import com.finalproject.ildoduk.entity.serviceCenter.CustomerAnswer;
import com.finalproject.ildoduk.repository.serviceCenter.CustomerAnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomerAnswerServiceImpl implements CustomerAnswerService {

    private final CustomerAnswerRepository customerAnswerRepository;


    //답글 저장
    @Override
    public void insertBoard(CustomerAnswerDTO dto) {
        CustomerAnswer entity = dtoToEntity(dto);

        customerAnswerRepository.save(entity);
    }
    //답글 목록
    @Override
    public CustomerAnswerDTO getAnswer(Long cusNo) {

       Optional<CustomerAnswer> result = customerAnswerRepository.findByCusNoCusNo(cusNo);


       return result.isPresent() ? entityToDto(result.get()) : null;
    }
    //답글 삭제
    @Override
    public Long deleteComment(Long aNo) {
        Optional<CustomerAnswer> result = customerAnswerRepository.findById(aNo);

        CustomerAnswer entity = result.get();
        customerAnswerRepository.delete(entity);

        CustomerAnswerDTO cusNo = entityToDto(entity);

        return cusNo.getCusNo();
    }


}
