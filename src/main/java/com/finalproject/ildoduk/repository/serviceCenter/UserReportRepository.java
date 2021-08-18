package com.finalproject.ildoduk.repository.serviceCenter;

import com.finalproject.ildoduk.dto.serviceCenter.UserReportDTO;
import com.finalproject.ildoduk.entity.pay.Payment;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    Optional<UserReport> findAllByIdId(String id);

    Page<UserReport> findAllByIdId(String id, Pageable pageable);

    //reportState = 1
    @Query(value = "SELECT u FROM UserReport u WHERE u.reportState = '1'")
    Page<UserReport> findAllOne(Pageable pageable);

    //reportState = 2
    @Query(value = "SELECT u FROM UserReport u WHERE u.reportState = '2'")
    Page<UserReport> findAllTwo(Pageable pageable);

    //신고 대상 친절점수
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE HelperInfo h SET h.kindness = h.kindness - 1 WHERE h.memberId.id = ?1")
    void minusKindness(String id);

}
