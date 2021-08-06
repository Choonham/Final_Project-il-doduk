package com.finalproject.ildoduk.controller.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/manager")
@Controller
@RequiredArgsConstructor
@Log4j2
public class ManagerController {


    //헬퍼 가입 승인 관련
    @GetMapping("/helperManagement")
    public void helperManagement(){
        //헬퍼 신청 목록 을 가져와야한다.


    }

}
