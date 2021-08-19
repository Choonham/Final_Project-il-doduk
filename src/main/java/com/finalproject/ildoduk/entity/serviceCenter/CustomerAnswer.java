package com.finalproject.ildoduk.entity.serviceCenter;

import com.finalproject.ildoduk.entity.BaseEntity;
import com.finalproject.ildoduk.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_answer_tbl")
@DynamicInsert
public class CustomerAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aNo;

    @OneToOne
    private CustomerBoard cusNo;

    @Column(nullable = false)
    private String aContent;

    @ManyToOne
    private Member aWriter;
}
