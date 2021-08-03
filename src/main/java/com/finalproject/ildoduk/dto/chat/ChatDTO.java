package com.finalproject.ildoduk.dto.chat;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ChatDTO {

    private Long No;
    private String message;
    private String recive;
    private String send;
    private String time;


}
