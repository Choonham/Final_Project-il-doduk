package com.finalproject.ildoduk.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberHelperInfoDTO {

    private HelperInfoDTO helperInfoDTO;
    private MemberDto memberDto;

}
