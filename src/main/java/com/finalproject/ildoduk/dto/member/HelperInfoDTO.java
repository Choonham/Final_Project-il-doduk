package com.finalproject.ildoduk.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelperInfoDTO {
    private long helperNo;
    private String memberId;
    private String goodAtFirst;
    private String goodAtSecond;
    private String goodAtThird;
    private int kindness;
    private String appeal;
    private String facePhoto;
    private String idCard;

}
