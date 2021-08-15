package com.finalproject.ildoduk.service.serviceCenter.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.serviceCenter.UserReportDTO;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;
import com.finalproject.ildoduk.repository.serviceCenter.UserReportRepository;
import com.finalproject.ildoduk.service.serviceCenter.service.UserReportService;
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
public class UserReportServiceImpl implements UserReportService {

    private final UserReportRepository userReportRepository;

    //신고 작성
    @Override
    public void insertReport(UserReportDTO userReportDTO) {

        UserReport entity = dtoToEntity(userReportDTO);

        userReportRepository.save(entity);
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

    //신고글 삭제
    @Override
    public void reportDelete(UserReportDTO userReportDTO) {
        Optional<UserReport> result = userReportRepository.findById(userReportDTO.getReportNo());
        userReportRepository.delete(result.get());
    }

//-------     관리자  :  신고글 전체 보기
//reportState = 1
    @Override
    public PageResultsDTO<UserReportDTO, UserReport> getStateOne(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("reportNo").descending());
        Page<UserReport> result = userReportRepository.findAllOne(pageable);

        Function<UserReport, UserReportDTO> fn = (entity -> entityToDto(entity));

        return new PageResultsDTO<>(result, fn);
    }
//reportState = 2
    @Override
    public PageResultsDTO<UserReportDTO, UserReport> getStateTwo(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("reportNo").descending());
        Page<UserReport> result = userReportRepository.findAllTwo(pageable);

        Function<UserReport, UserReportDTO> fn = (entity -> entityToDto(entity));

        return new PageResultsDTO<>(result, fn);
    }

    //관리자 :  확인 시에 신고 상황 업데이트
    @Override
    public void updateReportState(UserReportDTO userReportDTO) {

        Optional<UserReport> result = userReportRepository.findById(userReportDTO.getReportNo());

        UserReport entity = result.get();

        entity.changeReportState(userReportDTO.getReportState());

        userReportRepository.save(entity);
    }
    //친절 점수 깍음
    @Override
    public void minusKindness(UserReportDTO userReportDTO) {
        userReportRepository.minusKindness(userReportDTO.getReportTarget());
    }


}
