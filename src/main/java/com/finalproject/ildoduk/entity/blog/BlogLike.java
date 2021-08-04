package com.finalproject.ildoduk.entity.blog;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BlogLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeNo;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Blog blog;

    @Column(length=30, nullable = false)
    private String liker;

}
