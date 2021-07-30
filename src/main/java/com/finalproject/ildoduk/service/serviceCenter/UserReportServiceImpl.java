package com.finalproject.ildoduk.service.serviceCenter;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.pay.TradeHistoryDTO;
import com.finalproject.ildoduk.dto.serviceCenter.CustomerBoardDTO;
import com.finalproject.ildoduk.dto.serviceCenter.UserReportDTO;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.pay.TradeHistory;
import com.finalproject.ildoduk.entity.serviceCenter.CustomerBoard;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;
import com.finalproject.ildoduk.repository.member.MemberRepository;
import com.finalproject.ildoduk.repository.pay.TradeRepository;
import com.finalproject.ildoduk.repository.serviceCenter.UserReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserReportServiceImpl implements UserReportService{

    private final UserReportRepository userReportRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;


    //신고 작성
    @Override
    public void insertReport(UserReportDTO userReportDTO) {

        UserReport entity = dtoToEntity(userReportDTO);

        userReportRepository.save(entity);
    }


    //나와 거래했던 유저 정보 검색
    @Override
    public TradeHistoryDTO getUser(String id) {


        //내 거래 목록 조회
        Optional<TradeHistory> trade = tradeRepository.findAllByIdId(id);

        //조회된 결과가 있을 경우
        TradeHistory trade_result = trade.get();

        return trade.isPresent() ? entityToDtoTrade(trade_result) : null;
    }


    //신고글 리스트
    @Override
    public PageResultsDTO<UserReportDTO, UserReport> getReportList(UserReportDTO userReportDTO,PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("reportNo").descending());

        Page<UserReport> result = userReportRepository.findAllByIdId(userReportDTO.getId(),pageable);

        Function<UserReport, UserReportDTO> fn = (entity -> entityToDto(entity));

        return new PageResultsDTO<>(result, fn);
    }


    //신고글 상세보기
    @Override
    public UserReportDTO badUserReportDetail(UserReportDTO userReportDTO) {

        Optional<UserReport> result = userReportRepository.findById(userReportDTO.getReportNo());

        return result.isPresent() ? entityToDto(result.get()) : null;
    }


}
