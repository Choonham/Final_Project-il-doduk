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
public class CustomerBoardDTO {

    private Long cusNo;

    private String cusTitle;
    private String cusContent;
    private String cusWriter;
    private String cusNickName;

    private String secretBoard;
    private String passwordBoard;
    private String answerCheck;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
