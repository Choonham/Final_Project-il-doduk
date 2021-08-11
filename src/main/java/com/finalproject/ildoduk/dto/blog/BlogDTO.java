package com.finalproject.ildoduk.dto.blog;


import com.finalproject.ildoduk.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogDTO {
    private Long postNo;
    private String title;
    private String content;
    private Member writer;
    private int workNo;
    private String thumbnail;
    private LocalDateTime regDate, modDate;
}
