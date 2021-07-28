package com.finalproject.ildoduk.entity.blog;

import com.finalproject.ildoduk.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Blog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @Column(length = 300, nullable = false)
    private String title;

    @Column(length = 10000, nullable = false)
    private String content;

    @Column(length = 30, nullable = false)
    private String writer;

    @Column(length = 2000, nullable = true)
    private String thumbnail;

    @Column
    private int workNo;

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeWork(int workNo){
        this.workNo = workNo;
    }
}
