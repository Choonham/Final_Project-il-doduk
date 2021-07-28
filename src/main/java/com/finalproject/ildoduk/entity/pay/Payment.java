package com.finalproject.ildoduk.entity.pay;

import com.finalproject.ildoduk.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_tbl")
@DynamicInsert
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_no")
    private Long pointNo;

    @Column(name = "user_Id")
    private String userId;

    @Column(name = "total_point")
    @ColumnDefault("0")
    private Long totalPoint;


    //y -> 결제완료
    //n -> 환불완료
    @Column(name = "pay_check")
    @ColumnDefault("'y'")
    private String payCheck;

    //결제 이력 수정
    public void updatePayCheck(String payCheck){ this.payCheck = payCheck;}

}
