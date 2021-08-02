package com.finalproject.ildoduk.service.pay.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.entity.pay.Payment;
import com.finalproject.ildoduk.repository.pay.PaymentRepository;
import com.finalproject.ildoduk.service.pay.service.PaymentService;
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
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;


    //포인트 충전
    @Override
    public void pointCharge(PaymentDTO dto) {

        Payment entity = dtoToEntity(dto);

        paymentRepository.save(entity);

    }

//----------------------       해당 계정의 결제 이력을 조회한다. -----------------
    //결제 이력 페이지
    @Override
    public PageResultsDTO<PaymentDTO, Payment> getHistory(String test, PageRequestDTO pageRequestDTO) {

        log.info("결제 이력 조회하는 로직 내부 중" + test);

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("pointNo").descending());


        Page<Payment> result = paymentRepository.findAllByUserIdId(test,pageable);

        Function<Payment,PaymentDTO> fn = (entity -> entityToDto(entity));

        return new PageResultsDTO<>(result,fn);
    }



//-------------------------        환불            ------------------------
    //환불 창을 위한 정보 꺼내기
    @Override
    public PaymentDTO toRefund(Long pointNo){

        Optional<Payment> result = paymentRepository.findAllByPointNo(pointNo);

        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    //결제 이력 수정
    @Override
    public void updatePayCheck(PaymentDTO dto) {

        //아이디에 해당하는 데이터 조회
        Optional<Payment> result = paymentRepository.findById(dto.getPointNo());

        if(result.isPresent()){
        Payment payment = result.get();
            //값이 존재 한다면 결제이력 y -> n 으로 수정
            payment.updatePayCheck(dto.getPayCheck());

            paymentRepository.save(payment);
        }


    }


}
