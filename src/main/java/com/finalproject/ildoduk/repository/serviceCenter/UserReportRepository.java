package com.finalproject.ildoduk.repository.serviceCenter;

import com.finalproject.ildoduk.dto.serviceCenter.UserReportDTO;
import com.finalproject.ildoduk.entity.pay.Payment;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    Optional<UserReport> findAllByIdId(String id);

    Page<UserReport> findAllByIdId(String id, Pageable pageable);
}
