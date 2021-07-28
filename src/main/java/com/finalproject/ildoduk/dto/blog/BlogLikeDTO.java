package com.finalproject.ildoduk.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogLikeDTO {
    private Long likeNo;
    private Long postNo;
    private String liker;
}
