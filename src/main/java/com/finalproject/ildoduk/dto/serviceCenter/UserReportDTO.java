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
public class UserReportDTO {

    private Long reportNo;

    private String id;
    private String reportTarget;

    private String reportTitle;
    private String reportContent;
    private String reportKind;
    private String reportState;

    private int kindness;
    private LocalDateTime regDate;
}
