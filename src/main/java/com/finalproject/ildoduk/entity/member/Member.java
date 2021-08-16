package com.finalproject.ildoduk.entity.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.blog.BlogComment;
import com.finalproject.ildoduk.entity.blog.BlogLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity{

    @Id
    @Column(name="id", nullable = false)
    private String id;          //아이디

    @Column(name="name")
    private String name;        //이름

    @Column(name="gender")
    private String gender;      //성별

    @Column(name="birth")
    private String birth;       //생일

    @Column(name="nickname", unique = true)
    private String nickname;    //닉네임

    @Column(name="sido")
    private String sido;        //주소 시도 포함

    @Column(name="sigungu")
    private String sigungu;     //주소 시군구 포함

    @Column(name="address")
    private String address;     //지번주소

    @Column(name="phone")
    private String phone;       //연락처

    @Column(name="point")
    private int point;          //캐쉬포인트

    @Column(name="photo")
    private String photo;       //사진

    @Column(name="intro", length=1000)
    private String intro;       //유저 소개

    @Column(name = "state")
    private int state;          // 구분(0: 관리자, 1: 일반, 2: 헬퍼)


    // ================ Blog Cascade 설정을 위한 연관관계 설정(member 테이블에 추가되는 칼럼 x) ======//
    @JsonIgnore
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Blog> blog;

    @JsonIgnore
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<BlogComment> blogComment;

    @JsonIgnore //상호 참조를 없애버리기 위함
    @OneToMany(mappedBy = "liker", cascade = CascadeType.ALL)
    private List<BlogLike> blogLike;
    // ================ Blog Cascade 설정을 위한 연관관계 설정 끝 ======//

    /**정보수정**/

    public void changeNickname(String nickname){this.nickname = nickname;}

    public void changePhone(String phone){this.phone = phone;}

    public void changeAddress(String address){ this.address = address; }

    public void changeSido(String sido){this.sido= sido;}

    public void changeSigungu(String sigungu){this.sigungu= sigungu;}

    public void changePhoto(String photo){ this.photo = photo; }

    public void changeIntro(String intro){ this.intro = intro; }

        public void changeState(int state){ this.state = state;}


}
