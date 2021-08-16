package com.finalproject.ildoduk.repository.auction;

import com.finalproject.ildoduk.entity.auction.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Repository
@Transactional
public interface WeatherCodeRepository extends JpaRepository<WeatherCode, String>, QuerydslPredicateExecutor<WeatherCode> {

}
