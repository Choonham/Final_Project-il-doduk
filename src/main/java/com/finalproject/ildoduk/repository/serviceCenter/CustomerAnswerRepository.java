package com.finalproject.ildoduk.repository.serviceCenter;

import com.finalproject.ildoduk.entity.serviceCenter.CustomerAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface CustomerAnswerRepository extends JpaRepository<CustomerAnswer, Long>, QuerydslPredicateExecutor<CustomerAnswer> {
    Optional<CustomerAnswer> findByCusNoCusNo(Long dto);
}
