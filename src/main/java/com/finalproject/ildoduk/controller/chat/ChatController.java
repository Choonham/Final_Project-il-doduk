package com.finalproject.ildoduk.controller.chat;


import com.finalproject.ildoduk.dto.chat.ChatAucDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.entity.member.Member;
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
    public void chatList(Model model,HttpServletRequest request){
        //헬퍼 와 고객 모두 상관 없이 전리스트 출력중

    HttpSession session = request.getSession();
    MemberDto dto=(MemberDto) session.getAttribute("user");
    List<ChatAucDTO> list=chatService.get_chatUI(dto.getId());
    model.addAttribute("list",list);

    }



    @GetMapping("/chatting")
    public void chat(@RequestParam("id") String id, @RequestParam("auc") String auc, Model model, HttpServletRequest request){
        model.addAttribute("id", id);
        HttpSession session = request.getSession();
        model.addAttribute("user", session.getAttribute("user"));
        MemberDto dto =(MemberDto)session.getAttribute("user");
        session.setAttribute("userID", dto.getId());
        System.out.println("sessionID:"+ dto.getId());
        Member member = Member.builder().id(id).build();
        Long l_auc= Long.parseLong(auc);
        model.addAttribute("list",chatService.get_chatList(member,dto.getId(),l_auc));
        model.addAttribute("auc", l_auc);

    }

}
