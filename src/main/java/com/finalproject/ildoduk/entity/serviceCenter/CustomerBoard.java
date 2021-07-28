package com.finalproject.ildoduk.entity.serviceCenter;


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
@Table(name = "customer_board_tbl")
@DynamicInsert
public class CustomerBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cus_no")
    private Long cusNo;

    private String cusTitle;
    private String cusContent;
    private String cusWriter;

    @ColumnDefault("'n'")
    private String secretBoard;

    private String passwordBoard;

    @ColumnDefault("'n'")
    private String deleteCusBoard;
    @ColumnDefault("'n'")
    private String answerCheck;

    public void changeTitle(String cusTitle){
        this.cusTitle = cusTitle;
    }

    public void changeContent(String cusContent){
        this.cusContent = cusContent;
    }

    public void changeDelete(String deleteCusBoard){ this.deleteCusBoard = deleteCusBoard;}

    public void changeAnswerCheck(String answerCheck){ this.answerCheck = answerCheck;}
}
