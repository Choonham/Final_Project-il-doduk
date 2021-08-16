package com.finalproject.ildoduk.entity.auction;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@DynamicInsert
public class WeatherCode {

    @Id
    private String code;

    private String sido;
    private String sigungu;

}
