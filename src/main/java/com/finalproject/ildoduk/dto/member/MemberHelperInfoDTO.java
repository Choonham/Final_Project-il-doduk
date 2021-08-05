package com.finalproject.ildoduk.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberHelperInfoDTO {
    private String id;          //아이디(이메일)
    private String name;        //이름
    private String gender;      //성별
    private String nickname;    //닉네임
    private String photo;       //사진
    private String intro;       //유저 소개

    private int kindness;
    private String appeal;
    private String goodAtFirst;
    private String goodAtSecond;
    private String goodAtThird;
}
