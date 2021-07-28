package com.finalproject.ildoduk.repository.pay;


import com.finalproject.ildoduk.entity.pay.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, QuerydslPredicateExecutor<Payment> {

    Page<Payment> findAllByUserIdId(String id, Pageable pageable);

    Optional<Payment> findAllByPointNo(Long pointNo);



}
