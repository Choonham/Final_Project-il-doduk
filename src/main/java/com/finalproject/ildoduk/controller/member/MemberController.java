package com.finalproject.ildoduk.controller.member;

import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.service.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private MemberService service;

    //유저 회원가입 이동
    @GetMapping("/userRegister")
    public void userRegister(){

    }

    //유저 회원가입
    @PostMapping("/userRegister")
    public String userRegister(MemberDto dto){

        service.userRegister(dto);
        System.out.println(dto.getId());
        System.out.println(dto.getPwd());

        return "/member/kakao";
    }

    //유저 정보 조회
    @GetMapping("/userSearch")
    public void userSearch(String id, @ModelAttribute("userDto") MemberDto userDto, Model model){

        log.info("userSearch id ::" + id);

        MemberDto dto = service.userIdCheck(id);

        model.addAttribute("dto",dto);

    }

    //유저 가입여부 확인
    @PostMapping("/userCheck")
    public String userCheck(String id, HttpServletRequest request){

        log.info("userCheck id :: " + id);

        MemberDto dto = service.userIdCheck(id);

        HttpSession session = request.getSession();

        if(dto != null){
            session.setAttribute("user",dto.getId());
            return "/index";
        }else{
            return "/member/userRegister";
        }

    }


    //유저 수정
    @PostMapping("/userModify")
    public String userModify(MemberDto dto){

        service.userModify(dto);

        return "index";
    }

    //유저 삭제
    @PostMapping("/userDelete")
    public String userDelete(String id , RedirectAttributes requestAttributes){

        log.info("id ::  " + id);

        service.userDelete(id);

        requestAttributes.addFlashAttribute("msg","정상적으로 회원 탈퇴 되었습니다.");

        return "/index";
    }

    //유저 로그아웃
/*    @GetMapping("/userLogout")
    public void userLogout(HttpServletRequest request) throws Exception{

        HttpSession session = request.getSession();

        session.getAttribute(session.getId())
    }*/

    //카카오 로그인 페이지
    @GetMapping("/kakao")
    public void kakao(){

    }
    //카카오 로그인 세션 전달
    @ResponseBody
    @PostMapping(value = "/kakao2", produces = "application/json; charset=utf8")
    public String kakao(@RequestBody String json, HttpServletRequest request, Model model){
        //@RequestParam으로 js (json)의 값을 받고
        System.out.println(json); //js값 확인  json 문자열로 들어온 걸 parse로 쪼개야함


        MemberDto dto = service.kakaoLogin(json);//카카오 JSON에서 꺼낸 아이디 값

        log.info("dto Id:::::: " + dto.getId());

        MemberDto dto1 = service.userIdCheck(dto.getId());//db에서 꺼낸 아이디값


        if (dto1!=null) {

            HttpSession session = request.getSession();
            session.setAttribute("user", dto1);
            return "index";
        }

        else{

            model.addAttribute("msg", "회원가입 페이지로 이동 합니다");
            return "userRegister";
        }

    }


}
