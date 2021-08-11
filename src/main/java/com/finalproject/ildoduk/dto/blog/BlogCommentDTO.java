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
public class BlogCommentDTO {
    private Long commentNo;
    private Long postNo;
    private Member writer;
    private String content;
    private LocalDateTime regDate;
}
