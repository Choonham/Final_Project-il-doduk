package com.finalproject.ildoduk.controller.auction;

import com.finalproject.ildoduk.dto.*;
import com.finalproject.ildoduk.dto.auction.*;
import com.finalproject.ildoduk.dto.member.*;
import com.finalproject.ildoduk.entity.auction.*;
import com.finalproject.ildoduk.service.auction.service.*;
import com.google.gson.*;
import lombok.*;
import lombok.extern.log4j.*;
import org.apache.commons.io.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import javax.servlet.http.*;
import java.io.*;
import java.time.*;
import java.util.*;

@Controller
@RequestMapping("/auction")
@Log4j2
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @GetMapping("/main")
    public void main(PageRequestDTO pageRequestDTO){
        //경매 리스트 시간에 따라 상태값 변화 하기
        auctionService.changeState1(pageRequestDTO); //경매 시간 끝난 것 state=1로 변경
        auctionService.changeState2(pageRequestDTO); //일 수행시간 초과하고, 매칭이 안된 것 state=4로 변경
    }

    //리스트 출력 관련 컨트롤러 시작
    @GetMapping("/onAuctionList")
    public void list1(PageRequestDTO pageRequestDTO, Model model,HttpSession session) {
        //메인에서 user 값 받아서 리스트 출력
        log.info("======= list ========");

        MemberDto member = (MemberDto) session.getAttribute("user");
        String user = member.getId();
        System.out.println(user);
        //경매진행 중 리스트

        model.addAttribute("onAuctionList", auctionService.getList1(pageRequestDTO, user));
    }

    @GetMapping("/auctionDoneList")
    public void list2(PageRequestDTO pageRequestDTO, Model model,HttpSession session) {

        //메인에서 user 값 받아서 리스트 출력
        log.info("======= list ========");
        MemberDto member = (MemberDto) session.getAttribute("user");
        String user = member.getId();
        System.out.println(user);

        //경매 완료, 매칭 미완료
        model.addAttribute("auctionDoneList", auctionService.getList2(pageRequestDTO, user));

    }

    @GetMapping("/matchedAuctionList")
    public void list3(PageRequestDTO pageRequestDTO, Model model,HttpSession session) {
        //메인에서 user 값 받아서 리스트 출력
        log.info("======= list ========");
        MemberDto member = (MemberDto) session.getAttribute("user");
        String user = member.getId();
        System.out.println(user);
        //경매 완료, 매칭완료
        model.addAttribute("matchedAuctionList", auctionService.getList3(pageRequestDTO, user));
    }

    @GetMapping("/allDoneList")
    public void list4(PageRequestDTO pageRequestDTO, Model model,HttpSession session) {
        //메인에서 user 값 받아서 리스트 출력
        log.info("======= list ========");
        MemberDto member = (MemberDto) session.getAttribute("user");
        String user = member.getId();
        System.out.println(user);
        //경매 완료, 일 수행 완료
        model.addAttribute("allDoneList", auctionService.getList4(pageRequestDTO, user));
    }

    /** 경매 참여 가능한 옥션리스트 보기 **/
    @GetMapping("/availableAcutions")
    public void list5(PageRequestDTO pageRequestDTO, Model model,HttpServletRequest request){

        // 페이지에서 값 받아오기
        String sido = "";
        String sigungu = "";
        int category = 0;


        model.addAttribute("availableAuctions",auctionService.getAvailableAuctions(sido,sigungu,category ,pageRequestDTO));
    }

    //리스트 출력 관련 컨트롤러 끝

    //경매상세보기 - 진행 중 경매 혹은 매칭 미완료
    @GetMapping("/getOnAuction")
    public void getAuction1(Long aucSeq, Model model, PageRequestDTO pageRequestDTO){
        //옥션 정보
        model.addAttribute("auction",auctionService.getAuction(aucSeq));

        //타이머 정보 보내기
        model.addAttribute("time",auctionService.timer(aucSeq));

        //비딩 정보
        PageResultsDTO<BiddingListDTO,BiddingList> bidding = auctionService.getBidding(pageRequestDTO,aucSeq);
        model.addAttribute("biddingList",bidding);
        boolean exist = false;
        if( bidding.getDtoList().size() > 0 ){ exist = true; }
        model.addAttribute("exist",exist);
    }

    //경매상세보기 - 매칭 완료 또는 일 수행 완료
    @GetMapping("/getAuction")
    public void getAuction2(Long aucSeq, Model model, PageRequestDTO pageRequestDTO){
        //옥션 정보
        AuctionList auction = auctionService.getAuction(aucSeq).get();
        model.addAttribute("auction",auction);
        //낙찰 비딩 정보
        model.addAttribute("chosenBidding",auctionService.chosenBidding(aucSeq).get());
        //그외 정보
        /*if (auction.getState() != 3){

            boolean isAllDone = false;
        }*/
    }

    //목록에서 연결되는 버튼 처리 - 낙찰, 삭제, 채팅, 리뷰, 비즈니스카드보기

    //경매 등록 시작
    @GetMapping("/register")
    public void regist(Model model) {

        //일 등록 시 오늘 날짜보다 이전 선택불가
        String td = auctionService.today();
        System.out.println(td);
        model.addAttribute("td", td);

    }

    @PostMapping("/register")
    public String registerPost(AuctionListDTO dto,HttpServletRequest request) {

        log.info("register "+dto.toString());
        String date = request.getParameter("doDateT");
        System.out.println(date);
        LocalDateTime dodateTime = LocalDateTime.parse(date);
        dto.setDoDateTime(dodateTime);
        //새로 추가된 엔티티번호 받아서 출력하고 싶으면 하던가,,,,
        Long aucSeq = auctionService.register(dto);
        log.info(aucSeq);

        return "redirect:/auction/main";
    }


    //이미지 업로드 관련 시작
    // 파일 업로드
    @ResponseBody
    @PostMapping(value = "/fileUpload" ,produces = "application/json; charset=utf8")
    public String upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("postNo") String postNo) throws Exception {

        JsonObject jsonObject = new JsonObject();
        String fileRoot = "C:\\summernote_image\\";  // 파일이 저장될 절대 경로(상대 경로로 가능, 편의상 임시로 해놓은 것)
        //Long no = Long.parseLong(postNo);
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();    // request받은 multipart를 저장할 수 있게끔 InputStream 생성
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장

            jsonObject.addProperty("url", "/resources/fileupload/"+savedFileName); // contextroot + resources + 저장할 내부 폴더명
            jsonObject.addProperty("responseCode", "success");
            jsonObject.addProperty("savedFileName", savedFileName);
            jsonObject.addProperty("originalFileName", originalFileName);

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    // 양식에 올려놓은 파일 제거
    @ResponseBody
    @PostMapping(value = "/fileDelete", produces = "application/json; charset=utf8")
    public String delete(@RequestParam("filePathName") String filePathName){ // img 태그 내에 src 부분을 String 으로 전부 넘김

        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\summernote_image\\";  // 파일이 저장된 절대 경로

        log.info(filePathName); //  => ex: /resources/fileupload/67b7876f-976a-4c66-a835-99dd4f09eb4e.jpg
        String[] filePathNameSplit = filePathName.split("/"); // "/" 를 기준으로 split

        String fileServerPath = fileRoot + filePathNameSplit[3]; // 파일의 실제 이름

        File fileInServer = new File(fileServerPath); // 파일 객체 생성

        try{
            if(fileInServer.exists()){ // 파일이 있다면,
                fileInServer.delete(); // 파일 삭제
                log.info("file has been deleted!");
            }
            else {
                log.info("file does not exist!");
            }
            jsonObject.addProperty("removedFileName", filePathNameSplit[3]);
        } catch(Exception e){
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    //이미지 관련 컨트롤러 끝

}
