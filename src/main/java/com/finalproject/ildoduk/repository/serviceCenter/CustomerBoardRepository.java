package com.finalproject.ildoduk.repository.serviceCenter;

import com.finalproject.ildoduk.entity.serviceCenter.CustomerBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CustomerBoardRepository extends JpaRepository<CustomerBoard, Long>, QuerydslPredicateExecutor<CustomerBoard> {


}
