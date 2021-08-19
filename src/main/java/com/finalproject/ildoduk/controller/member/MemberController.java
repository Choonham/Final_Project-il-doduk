package com.finalproject.ildoduk.controller.member;

import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;

import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private MemberService service;


    private final HelperInfoService helperInfoService;


    /**===========================유저===============================**/

    //유저 회원가입 이동
    @GetMapping("/userRegister")
    public void userRegister(Model model){
        model.addAttribute("msg", "회원가입 페이지로 이동 합니다");
    }

    //유저 마이페이지 이동
    @GetMapping("/userMypage")
    public void userMypage(){

    }
    //헬퍼 마이페이지 이동
    @GetMapping("/helperMypage")
    public void helperMypage(){

    }


    //유저 회원가입
    @PostMapping("/userRegister")
    public String userRegister(MemberDto dto, @RequestParam("nickname") String nickname, Model model, HttpSession session){

        log.info("userRegister.html에서 닉네임 받아옴 ::: " + nickname);

        int cnt = service.nickNameCheck(nickname);  //DB에 같은 닉네임 있는지 확인  있으면 1 없으면 0

        if(cnt == 1 ){  //DB에 중복된 닉네임이 있을경우

            model.addAttribute("msg","닉네임이 중복되었습니다");

            return "/member/userRegister";
        }else{  //닉네임 중복이 없을 경우
            session.removeAttribute("user12");
            service.userRegister(dto);
            session.setAttribute("user",dto);
            MemberDto user = (MemberDto) session.getAttribute("user");
            System.out.println("::::::::::::::::::::::::::"+user.getState());


            return "redirect:/member/index";
        }


    }

    //유저 회원가입 간 닉네임 중복 확인
    @PostMapping(value = "/nicknameCheck" ,produces = "application/json; charset=utf8")
    @ResponseBody
    public int userNicknameCheck(@RequestBody String nickname, MemberDto memberDto){
         log.info("nicknameCheck ::: " + nickname);
         int cnt= service.nickNameCheck(nickname);

        log.info("nickname 결과확인 ::: " +cnt);

        return cnt;


    }

    //유저 정보 조회
    @GetMapping("/userSearch")
    public void userSearch(String id, @ModelAttribute("userDto") MemberDto memberDto, Model model){

        log.info("userSearch id ::" + id);

        MemberDto dto = service.userIdCheck(id);

        model.addAttribute("dto",dto);

    }

    //유저 가입여부 확인 (더미 테스트용)
    @PostMapping("/userCheck")
    public String userCheck(String id, HttpServletRequest request){

        log.info("userCheck id :: " + id);

        MemberDto dto = service.userIdCheck(id);

        HttpSession session = request.getSession();

        if(dto != null){
            session.setAttribute("user",dto);
            return "index";
        }else{
            return "member/userRegister";
        }

    }

    //유저 수정 페이지 이동
    @GetMapping("/userModify")
    public String userModifyPage(HttpSession session,Model model){
        MemberDto memberDto = (MemberDto)session.getAttribute("user");

        if(memberDto.getState() == 1){
            return "/member/userModify";
        } else {
            //헬퍼 정보 담아서 보내야한다.
            MemberHelperInfoDTO memberHelperInfoDTO = helperInfoService.helperFindById2(memberDto.getId());
            model.addAttribute("info", memberHelperInfoDTO);
            return "/member/helperModify";
        }
    }

    //유저 수정
    @PostMapping("/userModify")
    public String userModify(MemberDto dto, @RequestParam("nickname") String nickname, Model model){
        log.info("userModify.html에서 닉네임 받아옴 ::: " + nickname);
        int cnt= service.nickNameCheck(nickname);
        
        if(cnt == 1 ){  //DB에 중복된 닉네임이 있을경우

            model.addAttribute("msg","닉네임이 중복되었습니다 다시 작성해주세요");

            return "/member/userModify";

        }else{  //닉네임 중복이 없을 경우
            model.addAttribute("msg","개인정보가 수정되었습니다!");
            service.userModify(dto);

            return "/index";
        }
        
    }

    //유저 삭제
    @GetMapping("/userDelete")
    public String userDelete(@RequestParam("id")String id, Model model, HttpSession session){

        log.info("userDelete id ::  " + id);

        //state 3번으로 변경(삭제 동일한 기능 = 회원 탈퇴 후 개인정보 db유지);
        service.userDelete(id);

        session.invalidate();


        return "redirect:/member/DeleteToIndex";
    }

    //유저 로그아웃
    @GetMapping("/userLogout")
    public String userLogout(HttpServletRequest request) throws Exception{

        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/index";

    }

    @GetMapping("/index")
    public String userRegitoTheIndex(Model model){
        model.addAttribute("msg", "Hermes의 회원이 되신것을 축하합니다~");
        System.out.println("userRegitoTheIndex 확인");
        return "/index";
    }

    @GetMapping("/DeleteToIndex")
    public String userDeltoTheIndex(Model model){
        model.addAttribute("msg", "정상적으로 회원탈퇴 되었습니다!");
        System.out.println("userDeltoTheIndex 확인");
        return "/index";
    }

    @GetMapping("/stateToIndex")
    public String StateIndex(Model model){
        model.addAttribute("msg","회원 탈퇴된 계정입니다.");

        return "/index";
    }

    //카카오 로그인 페이지
    @GetMapping("/kakao")
    public void kakao(){

    }

    // 사용자 <-> 헬퍼 전환 시 세션 재조회..
    @GetMapping("/changeState")
    public String changeState(HttpSession session){
        MemberDto memberDto = (MemberDto)session.getAttribute("user");

        MemberDto member = service.userIdCheck(memberDto.getId());
        session.removeAttribute("user");
        session.setAttribute("user", member);

        return "/index";
    }

    //카카오 로그인 세션 전달
    @ResponseBody
    @PostMapping(value = "/kakao2", produces = "application/json; charset=utf8")
    public String kakao(@RequestBody String json, HttpServletRequest request, Model model){
        //@RequestParam으로 js (json)의 값을 받고
        System.out.println(json); //js값 확인  json 문자열로 들어온 걸 parse로 쪼개야함


        MemberDto dto = service.kakaoLogin(json);//카카오 JSON에서 꺼낸 아이디, 성별, 닉네임 값
        log.info("dto Id:::::: " + dto.getId());
        log.info("dto gender:::: " + dto.getGender());
        log.info("dto nick:::: " + dto.getNickname());
        log.info("dto photo::::" + dto.getPhoto());
        log.info("dto birth::::" + dto.getBirth());

        MemberDto dto1 = service.userIdCheck(dto.getId());//db에서 꺼낸 아이디에 대한 모든값
        HttpSession session = request.getSession();

        if (dto1!=null && dto1.getState() != 3) {
            session.setAttribute("user", dto1);
            return "/index";
        }
        else if(dto1!=null && dto1.getState() == 3){
            model.addAttribute("회원 탈퇴된 계정입니다");

            return "/member/stateToIndex";
        }else{
            dto.setState(1);
            session.setAttribute("user12",dto);

            return "/member/userRegister";
        }

    }
    /**===========================헬퍼===============================**/
    //헬퍼 가입신청 url
    @GetMapping("/helperRegister")
    public void helperRegisterPage(){

    }
    //헬퍼 가입전 신청여부 확인
    @GetMapping("/helperRegisterCheck")
    public String helperRegisterCheck(@RequestParam("memberId")String memberId, Model model){
        //helperRegister에서 받아온 아이디값으로 helperInfo와 member를 조회해야함.
        HelperInfoDTO count = helperInfoService.helperFindById(memberId); //helperInfo에 값 있는지 확인

        if(count == null) { //헬퍼의 agreehelper의 값이 없을 경우 (1: 신청대기중)
            model.addAttribute("memberId",memberId);
            return "redirect:/member/helperRegister";
        }else if(count.getAgreeHelper() == 1){ //헬퍼의 agreehelper의 값이 1일 경우 (1: 신청대기중)

            model.addAttribute("msg", "헬퍼 신청중인 상태입니다.");
            return "/index";
        }else if(count.getAgreeHelper() == 2) { //헬퍼의 agreehelper의 값이 2일 경우 (2: 헬퍼승인완료)
            model.addAttribute("memberId",memberId);
            return "redirect:/member/helperModify";
        }else{
            model.addAttribute("msg","헬퍼 승인이 반려되었습니다 다시 신청해주세요");

            return "redirect:/member/helperRegister";
        }
    }


    //헬퍼 가입신청
    @PostMapping(value="/helperRegister")
    public String helperRegister(@RequestParam("memberId") String memberId, @RequestParam("img") String img, HelperInfoDTO helperInfoDTO, Model model, HttpSession session){

        log.info("helperRegister member id :::: " + memberId);
        log.info("helperRegister multipartFile ::: " + img);

            int cnt = helperInfoService.helperRegister(helperInfoDTO);

            System.out.println("helperRegister 처리 확인 !!!!!" + cnt);
            if(cnt == 1) {

                model.addAttribute("msg", "정상적으로 헬퍼 신청이 완료되었습니다! ");
                return "/index";
            }else{
                model.addAttribute("msg","헬퍼가입이 정상적으로 이루어지지 않았습니다");

                return "/helperRegister";
            }

    }

    //헬퍼 정보 수정페이지
    @GetMapping("/helperModify")
    public void helperModifyPage(){

    }


    //헬퍼 정보 수정 agreeHelper == 2 일 경우만 해당
    @PostMapping("/helperModify")
    public String helperModify(@RequestParam("memberId")String memberId, Model model, HelperInfoDTO helperInfoDTO){
        log.info("helperModify memberID ::::::: " +  memberId);
            HelperInfoDTO dto = helperInfoService.helperFindById(memberId); //헬퍼 memberId로 찾아서 dto반환

            helperInfoService.helperModify(dto);// dto를 repository.save();

            model.addAttribute("msg","헬퍼 정보가 수정되었습니다!");
            helperInfoService.helperModify(helperInfoDTO);
            return "/index";
    }



    //게시글 아이디(or 닉네임)누르면 해당 헬퍼 정보 가져오기
    @GetMapping(value="/helperBizCard")
    public void helperSearch(@RequestParam("memberId")String memberId, Model model){
        log.info("helperSearch에 넘어온 아이디 확인" + memberId );
        //Member member = Member.builder().id(memberId).build();
        MemberHelperInfoDTO helperInfoDTO = helperInfoService.helperFindById2(memberId);
        log.info("helperBizCard :::: helperInfoDTO :::: " + helperInfoDTO);

        model.addAttribute("helperInfo", helperInfoDTO);

    }

}
