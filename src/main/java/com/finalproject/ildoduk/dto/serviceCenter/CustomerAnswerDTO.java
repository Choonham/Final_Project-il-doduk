package com.finalproject.ildoduk.dto.serviceCenter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAnswerDTO {

    private Long aNo;
    private Long cusNo;

    private String aContent;
    private String aWriter;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
