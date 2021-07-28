package com.finalproject.ildoduk.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogCommentDTO {
    private Long commentNo;
    private Long postNo;
    private String writer;
    private String content;
}
