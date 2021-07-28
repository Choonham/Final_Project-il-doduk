package com.finalproject.ildoduk.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long pointNo;
    private String userId;
    private int totalPoint;
    private String payCheck;
    private LocalDateTime regDate;

}
