package com.finalproject.ildoduk.service.serviceCenter;

import com.finalproject.ildoduk.dto.serviceCenter.UserReportDTO;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;
import com.finalproject.ildoduk.repository.serviceCenter.UserReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserReportServiceImpl implements UserReportService{

    private final UserReportRepository userReportRepository;

    //신고 작성
    @Override
    public void insertReport(UserReportDTO userReportDTO) {

        UserReport entity = dtoToEntity(userReportDTO);

        userReportRepository.save(entity);
    }
}
