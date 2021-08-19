package com.finalproject.ildoduk.entity.blog;

import com.finalproject.ildoduk.entity.BaseEntity;
import com.finalproject.ildoduk.entity.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @ManyToOne
    private Member writer;

    @Column(length = 2000, nullable = true)
    private String thumbnail;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<BlogComment> blogComment;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<BlogLike> blogLike;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<BlogFiles> blogFiles;


    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
