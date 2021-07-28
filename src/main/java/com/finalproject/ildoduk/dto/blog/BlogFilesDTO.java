package com.finalproject.ildoduk.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogFilesDTO {
    private Long fileNo;
    private String fileName;
    private String fileOrigin;
    private Long postNo;
}
