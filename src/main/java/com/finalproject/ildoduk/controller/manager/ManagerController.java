package com.finalproject.ildoduk.controller.manager;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/manager")
@Controller
@RequiredArgsConstructor
@Log4j2
public class ManagerController {

    private final MemberService memberService;
    private final HelperInfoService helperInfoService;


//-----------  헬퍼 가입 승인 관련  ---------------------
    @GetMapping("/helperManagement")
    public void helperManagement(PageRequestDTO pageRequestDTO, Model model){
        //헬퍼 신청 목록 을 가져와야한다.
        PageResultsDTO<HelperInfoDTO,HelperInfo> helperInfoDTO = helperInfoService.helperRequest(pageRequestDTO);

        model.addAttribute("helperManagement", helperInfoDTO);

    }

    //승인
    @GetMapping("/accept")
    public String acceptHelper(HelperInfoDTO helperInfoDTO){

        //helperInfo state => 2로 변경
        helperInfoService.accept(helperInfoDTO);
        //번호를 이용해서 값을 조회해야하나???
        HelperInfoDTO helperId = helperInfoService.helperInfo(helperInfoDTO);

        log.info("참조 아이디 ~~~~~~ "+helperId.getMemberId());

        //Member state = 2로 변경
        MemberDto memberDto = memberService.userIdCheck(helperId.getMemberId());
        memberService.updateState(memberDto);
        //헬퍼 승인 완료
        return "redirect:/manager/helperManagement";
    }

    //승인 반려
    @GetMapping("/deny")
    public String denyHelper(HelperInfoDTO helperInfoDTO){
        //반려시에 agreeHelper = 3으로 변경
        helperInfoService.deny(helperInfoDTO);
        //반려 사유...음...

        return "redirect:/manager/helperManagement";
    }

}
