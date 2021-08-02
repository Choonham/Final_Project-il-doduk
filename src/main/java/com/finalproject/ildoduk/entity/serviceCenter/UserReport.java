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
@DynamicInsert
@Table(name = "user_report_tbl")
public class UserReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportNo;

    @ManyToOne
    private Member id;

    @ManyToOne
    private Member reportTarget;

    private String reportTitle;
    private String reportContent;
    private String reportKind;
    private String reportState;

    public void changeReportState(String reportState){ this.reportState = reportState;}
}
