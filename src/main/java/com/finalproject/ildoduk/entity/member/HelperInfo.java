package com.finalproject.ildoduk.entity.member;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HelperInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long helperNo;

    @ManyToOne
    private Member memberId;

    @Column(length = 30)
    private String goodAtFirst;

    @Column(length = 30)
    private String goodAtSecond;

    @Column(length = 30)
    private String goodAtThird;

    @Column(length = 3)
    private int kindness;

    @Column(length = 30)
    private String appeal;

    @Column
    private String img;

    @Column
    @ColumnDefault("1")
    private int agreeHelper;

    /**정보수정**/
    public void changeAgreeHelper(int agreeHelper){ this.agreeHelper = agreeHelper;}

    //헬퍼 정보 수정
    public void changeGoodAtFirst(String goodAtFirst){this.goodAtFirst = goodAtFirst;}
    public void changeGoodAtSecond(String goodAtSecond){this.goodAtSecond = goodAtSecond;}
    public void changeGoodAtThird(String goodAtThird){this.goodAtThird = goodAtThird;}
    public void changeAppeal(String appeal){this.appeal = appeal;}
    public void changeImg(String img){this.img = img;}
}
