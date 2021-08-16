package com.finalproject.ildoduk.dto.auction;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {

    private String day; //0-오늘, 1-내일 , 2-모레
    private String hour; //시간
    private String wfKor; // 날씨
    private String temp; //온도
    private String pop; //강수확률

}
