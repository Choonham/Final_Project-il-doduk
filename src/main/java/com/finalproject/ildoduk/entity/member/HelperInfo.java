package com.finalproject.ildoduk.entity.member;

import lombok.*;

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


}
