package com.finalproject.ildoduk.dto.member;

import com.finalproject.ildoduk.entity.member.Member;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Log4j2
public class helperInfoDTO {



    private long helperNo;


    private Member memberId;


    private String goodAtFirst;


    private String goodAtSecond;


    private String goodAtThird;


    private int kindness;


    private String appeal;


}
