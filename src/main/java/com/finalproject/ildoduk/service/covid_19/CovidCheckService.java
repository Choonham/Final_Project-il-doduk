package com.finalproject.ildoduk.service.covid_19;

import com.finalproject.ildoduk.dto.covid.DisasterMsgDTO;
import com.finalproject.ildoduk.dto.covid.TotalCovidDTO;

import java.util.ArrayList;
import java.util.HashMap;

public interface CovidCheckService {
    // 코로나 전체 통계 확인 (누적 확진자, 사망자, 누적 확진률, 최근 10일 누적 확진자 수(기준일 전일까지))
    TotalCovidDTO totalCovid19Info();

    // 재난문자 기반 하루 실시간 확진자 (지역별 집계)
    DisasterMsgDTO dailyCovid19Info(String keyword, String sido);

    // 백신 접종 현황
    HashMap<String, Integer[]> vaccineInfo();
}
