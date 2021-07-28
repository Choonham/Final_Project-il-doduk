package com.finalproject.ildoduk.repository.pay;

import com.finalproject.ildoduk.entity.pay.TradeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TradeRepository extends JpaRepository<TradeHistory, Long>, QuerydslPredicateExecutor<TradeHistory> {

    Page<TradeHistory> findAllByIdId(String id, Pageable pageable);
}
