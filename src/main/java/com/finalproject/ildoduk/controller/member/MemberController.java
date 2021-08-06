package com.finalproject.ildoduk.controller.member;

import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.dto.member.UploadResultDTO;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
    public void userRegister(){

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
    public String userRegister(MemberDto dto, @RequestParam("nickname") String nickname, Model model){

        log.info("userRegister.html에서 닉네임 받아옴 ::: " + nickname);

        int cnt = service.nickNameCheck(nickname);  //DB에 같은 닉네임 있는지 확인  있으면 1 없으면 0

        if(cnt == 1 ){  //DB에 중복된 닉네임이 있을경우

            model.addAttribute("msg","닉네임이 중복되었습니다");

            return "/member/userRegister";
        }else{  //닉네임 중복이 없을 경우
            model.addAttribute("msg", "일도둑의 회원이 되신것을 축하합니다~");
            service.userRegister(dto);

            return "/index";
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
    public void userModifyPage(){

    }

    //유저 수정
    @PostMapping("/userModify")
    public String userModify(MemberDto dto, Model model){

        service.userModify(dto);
        model.addAttribute("msg","개인정보가 수정되었습니다!");
        return "index";
    }

    //유저 삭제
    @PostMapping("/userDelete")
    public String userDelete(String id , RedirectAttributes requestAttributes, HttpSession session){

        log.info("userDelete id ::  " + id);

        service.userDelete(id);
        session.invalidate();
        requestAttributes.addFlashAttribute("msg","정상적으로 회원 탈퇴 되었습니다.");

        return "/index";
    }

    //유저 로그아웃
    @GetMapping("/userLogout")
    public String userLogout(HttpServletRequest request) throws Exception{

        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/index";

    }

    @GetMapping("/index")
    public String totheindex(){

        return "../index";
    }

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


        MemberDto dto = service.kakaoLogin(json);//카카오 JSON에서 꺼낸 아이디, 성별, 닉네임 값

        log.info("dto Id:::::: " + dto.getId());
        log.info("dto gender:::: " + dto.getGender());
        log.info("dto nick:::: " + dto.getNickname());
        log.info("dto photo::::" + dto.getPhoto());
        log.info("dto birth::::" + dto.getBirth());

        MemberDto dto1 = service.userIdCheck(dto.getId());//db에서 꺼낸 아이디에 대한 모든값
        HttpSession session = request.getSession();

        if (dto1!=null) {

            session.setAttribute("user", dto1);
            return "/index";
        }

        else{
            session.setAttribute("user",dto);
            model.addAttribute("msg", "회원가입 페이지로 이동 합니다");
            return "/member/userRegister";
        }

    }
    /**===========================헬퍼===============================**/
    //헬퍼 가입신청 url
    @GetMapping("/helperRegister")
    public void helperRegisterPage(){

    }
    //헬퍼 가입신청
    @PostMapping("/helperRegister")
    public void helperRegister(@RequestParam("memberId")String memberId, HelperInfoDTO helperInfoDTO, Model model){
        log.info("helperRegister member id :::: " + memberId);

        //agreeHelper 신청 내용 DB에 저장
        helperInfoService.helperRegister(helperInfoDTO);

        int cnt = helperInfoService.helperRegisterIdCheck(memberId);


    }

    //헬퍼 가입확인
    @PostMapping("/helperIdCheck")
    public void helperIdCheck(@RequestParam("memberId")String memberId, HelperInfoDTO helperInfoDTO, MemberDto memberdto, Model model){
        log.info(" helperIdCheck memberId :: " + memberId);

        MemberDto memberDto= service.userToHelperIdCheck(memberId);
            log.info(" helperIdCheck userTOHelperIDCheck test :::: " + memberDto );

        HelperInfoDTO helperInfoDto = helperInfoService.helperFindById(memberId);
        log.info(" helperIdCheck helperFindById test :::: " + helperInfoDto );

        if(helperInfoDto.getAgreeHelper()==2){

            model.addAttribute("user",memberDto);
            model.addAttribute("user1",helperInfoDto);

            // return ??????
        }else{
            // return ??????
        }

    }

    //헬퍼 가입신청 파일 업로드

 /*   @Value("${com.finalproject.upload.path}")//application.properties의 변수
    private String uploadPath;

    @PostMapping("/member/helperUpload")
    public void helperUploadsFacePhoto(MultipartFile[] uploadFiles) {
        for(MultipartFile uploadFile:uploadFiles){
            //실제 파일이름 IE나 Edge는 전체경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\"));

            log.info("fileName : " + fileName);
            //날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator +
                    File.separator + uuid + "_" + fileName;
                    Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);//실제 이미지 저장
                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));

            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/mm/dd"));

        String folderPath = str.replace("/", File.separator);

        //make folder -----------------
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    @GetMapping("/member/display")
    public ResponseEntity<byte[]> getFile(String fileName){

        ResponseEntity<byte[]> result = null;

        try{
        String srcFileName = URLDecoder.
        }catch(Exception e){
            log.info(e.getMessage());
            return
        }

    }*/

}
