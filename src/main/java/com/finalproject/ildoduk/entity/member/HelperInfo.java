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

    @ManyToOne(cascade = CascadeType.ALL)
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
    private String facePhoto;

    @Column
    private String idCard;

    @Column
    @ColumnDefault("1")
    private int agreeHelper;


    public void changeAgreeHelper(int agreeHelper){ this.agreeHelper = agreeHelper;}
}
