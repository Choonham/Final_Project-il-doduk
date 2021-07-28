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
public class BlogComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;

    @ManyToOne
    private Blog blog;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String content;
}
