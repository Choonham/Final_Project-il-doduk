package com.finalproject.ildoduk.entity.blog;

import com.finalproject.ildoduk.entity.member.Member;
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

    @ManyToOne
    private Blog blog;

    @ManyToOne
    private Member liker;

}
