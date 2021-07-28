package com.finalproject.ildoduk.entity.serviceCenter;

import com.finalproject.ildoduk.entity.BaseEntity;
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

    private Long cusNo;


    private String aTitle;
    private String aContent;
    private String aWriter;
}
