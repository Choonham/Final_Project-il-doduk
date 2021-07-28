package com.finalproject.ildoduk.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class TempPageRequestDTO {
    private int tempPage; // 화면에 전달되는 페이지번호

    private String tempType;
    private String tempKeyword;
}
