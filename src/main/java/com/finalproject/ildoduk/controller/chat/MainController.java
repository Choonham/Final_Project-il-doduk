package com.finalproject.ildoduk.controller.chat;


import com.finalproject.ildoduk.service.chat.ChatService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {
public static int i=0;

@Autowired
ChatService chatService;

@Autowired
MemberService service;

    @GetMapping("/chatList")
    public void chatList(Model model){
    List<String> list=service.getList();
    model.addAttribute("clientList",list);

    }




}
