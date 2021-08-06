package com.finalproject.ildoduk.controller.manager;

import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/manager")
@Controller
@RequiredArgsConstructor
@Log4j2
public class ManagerController {

    private final MemberService memberService;
    private final HelperInfoService helperInfoService;


//-----------  헬퍼 가입 승인 관련  ---------------------
    @GetMapping("/helperManagement")
    public void helperManagement(Model model){
        //헬퍼 신청 목록 을 가져와야한다.
        HelperInfoDTO helperInfoDTO = helperInfoService.checkState();
        log.info(helperInfoDTO + "헬퍼 승인 요청~~~~~~~~~~2");

        model.addAttribute("helperManagement",helperInfoDTO);

    }

    //승인
    @GetMapping("/accept")
    public String acceptHelper(HelperInfoDTO helperInfoDTO){
        helperInfoDTO.setAgreeHelper(2);

        helperInfoService.accept(helperInfoDTO);

        return "redirect:/manager/helperManagement";
    }

    //승인 반려
    @GetMapping("/deny")
    public String denyHelper(HelperInfoDTO helperInfoDTO){
        helperInfoDTO.setAgreeHelper(3);

        helperInfoService.deny(helperInfoDTO);

        return "redirect:/manager/helperManagement";
    }

}
