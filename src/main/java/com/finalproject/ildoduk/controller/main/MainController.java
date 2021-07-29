package com.finalproject.ildoduk.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/index")
    public void goto_main(){
        
    }

    @GetMapping("/")
    public String main(){
        return "redirect:/member/kakao";
    }


}
