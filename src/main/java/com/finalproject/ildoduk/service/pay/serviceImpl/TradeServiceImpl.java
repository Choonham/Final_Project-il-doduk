package com.finalproject.ildoduk.service.pay.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.pay.TradeHistoryDTO;
import com.finalproject.ildoduk.entity.pay.TradeHistory;
import com.finalproject.ildoduk.repository.pay.TradeRepository;
import com.finalproject.ildoduk.service.pay.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@RequiredArgsConstructor
@Service
@Log4j2
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;


    //전체 거래 이력 조회회
    @Override
    public PageResultsDTO<TradeHistoryDTO, TradeHistory> allContents(TradeHistoryDTO tradeHistoryDTO, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("tNo").descending());
        log.info("거래 내역 조회 내부 로직1 : " + pageable);
        Page<TradeHistory> result = tradeRepository.findAllByIdId(tradeHistoryDTO.getId(), pageable);
        log.info("거래 내역 조회 내부 로직2 : " + result);
        Function<TradeHistory, TradeHistoryDTO> fn = (entity -> entityToDto(entity));
        log.info("거래 내역 조회 내부 로직 : " + fn);
       return new PageResultsDTO<>(result, fn);
    }





}
