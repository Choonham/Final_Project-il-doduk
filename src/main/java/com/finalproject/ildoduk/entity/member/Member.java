package com.finalproject.ildoduk.entity.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @Column(name="id", nullable = false)
    private String id;          //아이디

    @Column(name="pwd", nullable = false)
    private String pwd;         //패스워드

    @Column(name="name")
    private String name;        //이름

    @Column(name="gender")
    private String gender;      //성별

    @Column(name="birth")
    private String birth;       //생일

    @Column(name="nickname")
    private String nickname;    //닉네임

    @Column(name="address")
    private String address;     //주소

    @Column(name="phone")
    private String phone;       //연락처

    @Column(name="point")
    private int point;          //캐쉬포인트

    @Column(name="photo")
    private String photo;       //사진

    @Column(name="intro")
    private String intro;       //유저 소개

    @Column(name = "state")
    private int state;          // 구분(0: 관리자, 1: 일반, 2: 헬퍼)

    /**정보수정**/
    public void changePwd(String pwd){this.pwd = pwd;}

    public void changeNickname(String nickname){this.nickname = nickname;}

    public void changePhone(String phone){this.phone = phone;}

    public void changeAddress(String address){ this.address = address; }

    public void changePhoto(String photo){ this.photo = photo; }

    public void changeIntro(String intro){ this.intro = intro; }

}
