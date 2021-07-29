package com.finalproject.ildoduk.dto.chat;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RelationDTO {
    private long rel_No;

    private String send;
    private String accept;


}
