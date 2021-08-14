package com.finalproject.ildoduk.dto.covid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisasterMsgDTO {
    private ArrayList<String> msgList;
    private Set<String> sidoList;
}
