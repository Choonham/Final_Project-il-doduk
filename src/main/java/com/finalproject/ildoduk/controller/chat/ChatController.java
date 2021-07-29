package com.finalproject.ildoduk.controller.chat;


import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.service.chat.ChatService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {
public static int i=0;

@Autowired
ChatService chatService;

@Autowired
MemberService service;

/*==================================채팅시작========================================*/



    @GetMapping("/chatList")
    public void chatList(Model model){
        //헬퍼 고객 상관 없이 전리스트 출력중
        List<String> list=service.getList();
        model.addAttribute("clientList",list);

    }



    @GetMapping("/chatting")
    public void chat(@RequestParam("id") String id, Model model, HttpServletRequest request){
        model.addAttribute("id", id);
        HttpSession session = request.getSession();
        model.addAttribute("user", session.getAttribute("user"));
        MemberDto dto =(MemberDto)session.getAttribute("user");
        model.addAttribute("list",chatService.get_chatList(dto.getId(),id));
    }


  /*  @GetMapping("/chatting")
    public void chat(@RequestParam("id") String id, Model model, HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("id", id);
        HttpSession session = request.getSession();
        model.addAttribute("userID", (String)session.getAttribute("userID"));
        model.addAttribute("list",chatService.get_chatList((String)session.getAttribute("userID"),id));



    }*/






}
