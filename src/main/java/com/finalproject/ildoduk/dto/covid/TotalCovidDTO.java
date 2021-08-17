package com.finalproject.ildoduk.dto.covid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalCovidDTO {
    private String accDefRate;
    private int accDeathCnt;
    private int accDecideCnt;
    private int deathCntToday;
    private ArrayList<Integer> decideCntDays;
}
