package com.finalproject.ildoduk.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberHelperInfoDTO {

    private HelperInfoDTO helperInfoDTO;
    private MemberDto memberDto;

    /*MemberDto*/
    private String id;          //아이디(이메일)
    private String pwd;         //패스워드
    private String name;        //이름
    private String gender;      //성별
    private String birth;       //생일
    private String nickname;    //닉네임
    private String sido;        //주소 시도 포함
    private String sigungu;     //주소 시군구 포함
    private String address;     //지번 주소
    private String phone;       //연락처
    private int point;          //캐쉬포인트
    private String photo;       //사진
    private String intro;       //유저 소개
    private int state;          //구분
    private int state2;        //헬퍼 유저 전환 버튼
    private LocalDateTime regDate;  //가입일


    /*helperInfoDTO*/
    private long helperNo;          //헬퍼 정보 번호
    private String memberId;        //아이디
    private String goodAtFirst;     //경력사항1
    private String goodAtSecond;    //경력사항2
    private String goodAtThird;     //경력사항3
    private int kindness;           //친절점수
    private String appeal;          //각오
    private String facePhoto;       //본인 얼굴사진
    private String idCard;          //본인 신분증 사진


}
