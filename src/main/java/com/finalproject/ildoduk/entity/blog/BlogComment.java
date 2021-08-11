package com.finalproject.ildoduk.entity.blog;

import com.finalproject.ildoduk.entity.BaseEntity;
import com.finalproject.ildoduk.entity.member.Member;
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

    @ManyToOne
    private Member writer;

    @Column(nullable = false)
    private String content;
}
