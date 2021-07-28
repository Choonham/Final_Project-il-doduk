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
public class BlogFiles extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileNo;

    @Column(length = 200, nullable = true)
    private String fileName;

    @Column(length = 300, nullable = true)
    private String fileOrigin;

    @ManyToOne
    private Blog blog;
}
