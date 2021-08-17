package com.finalproject.ildoduk.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/index")
    public void goto_main(){
        
    }

    @GetMapping("/")
    public String main(){
        return "redirect:/index";
    }

    @RequestMapping("/map")
    public void map(@RequestParam("address") String address, Model model){

        model.addAttribute("address",address);


    }
}
