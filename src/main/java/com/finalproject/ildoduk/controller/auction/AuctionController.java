package com.finalproject.ildoduk.controller.auction;

import com.finalproject.ildoduk.dto.*;
import com.finalproject.ildoduk.dto.auction.*;
import com.finalproject.ildoduk.dto.member.*;
import com.finalproject.ildoduk.entity.auction.*;
import com.finalproject.ildoduk.entity.member.*;
import com.finalproject.ildoduk.service.auction.service.*;
import com.google.gson.*;
import lombok.*;
import lombok.extern.log4j.*;
import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final AuctionService auctionService;

    @GetMapping("/main")
    public void main(PageRequestDTO pageRequestDTO) {
        //경매 리스트 시간에 따라 상태값 변화 하기
        auctionService.changeState1(pageRequestDTO); //경매 시간 끝난 것 state=1로 변경
        auctionService.changeState2(pageRequestDTO); //미션 시작 시간(-30분) 초과하고, 매칭이 안된 것 state=4로 변경
    }

    //=================================================== User 리스트 ==================================================//
    //auc.state=0 경매 진행 중 / auc.state=1 경매완료, 매칭미완료 / auc.state=2 매칭완료 / auc.state=3 일 수행 완료 / auc.state=4 삭제

    //경매 진행 중 미션, 경매 완료된 미매칭 미션 페이지
    @GetMapping("/onAuctionList")
    public void list1(PageRequestDTO pageRequestDTO, Model model, HttpSession session, boolean isAuctionDone) {
        //메인에서 user 값 받아서 리스트 출력
        //log.info("======= list ========");

        //member 정보 얻기
        MemberDto member = (MemberDto) session.getAttribute("user");
        String user = member.getId();
        //System.out.println(user);

        //paging 설정
        pageRequestDTO.setSize(3);

        //페이지 설정 값 따로 만들어 주기
        PageRequestDTO pageRequestDTO2 = PageRequestDTO.builder().page(1).size(3).build();
        //경매 중인 리스트 isAuctionDone = false
        if (!isAuctionDone) {
            model.addAttribute("isAuctionDone", isAuctionDone);
            model.addAttribute("onAuctionList", auctionService.getList1(pageRequestDTO, user));
            model.addAttribute("auctionDoneList", auctionService.getList2(pageRequestDTO2, user));
        }

        //경매완료 리스트 isAuctionDone = true
        else if (isAuctionDone) {
            model.addAttribute("isAuctionDone", isAuctionDone);
            model.addAttribute("onAuctionList", auctionService.getList1(pageRequestDTO2, user));
            model.addAttribute("auctionDoneList", auctionService.getList2(pageRequestDTO, user));
        }
    }

    /* 사용하지 않음,혹시 실수로 연결될 경우 onAuctionList로 반환 */
    @GetMapping("/auctionDoneList")
    public String list2(HttpServletRequest request) {
        return "redirect:/auction/onAuctionList";
    }

    @GetMapping("/matchedAuctionList")
    public void list3(PageRequestDTO pageRequestDTO, Model model, HttpSession session,boolean isAllDone) {

        //메인에서 user 값 받아서 리스트 출력
        //log.info("======= list ========");
        MemberDto member = (MemberDto) session.getAttribute("user");
        String user = member.getId();
        //System.out.println(user);

        //paging 설정
        pageRequestDTO.setSize(3);

        //페이지 설정 값 따로 만들어 주기
        PageRequestDTO pageRequestDTO2 = PageRequestDTO.builder().page(1).size(3).build();
        //매칭 완료 경매 isAllDone = false
        if (!isAllDone) {
            model.addAttribute("isAllDone", isAllDone);
            model.addAttribute("matchedAuctionList", auctionService.getList3(pageRequestDTO, user));
            model.addAttribute("allDoneList", auctionService.getList4(pageRequestDTO2, user));
        }

        //미션 완료 리스트 isAllDone = true
        else if (isAllDone) {
            model.addAttribute("isAllDone", isAllDone);
            model.addAttribute("matchedAuctionList", auctionService.getList1(pageRequestDTO2, user));
            model.addAttribute("allDoneList", auctionService.getList2(pageRequestDTO, user));
        }

    }

    /* 사용하지 않음,혹시 실수로 연결될 경우 matchecdAuctionList로 반환 */
    @GetMapping("/allDoneList")
    public String list4() {
        return "redirect:/auction/matchedAuctionList";
    }


    //=================================================== Helper 리스트 ==================================================//

    /*낙찰 된 미션, 내가 참여한 미션 보기*/

    //내가 참여한 미션
    @GetMapping("/myBidsHelper")
    public void MyBidsHelper(PageRequestDTO pageRequestDTO, Model model, HttpSession session, boolean onAuction) {

        //메인에서 user 값 받아서 리스트 출력
        //log.info("======= list ========");
        MemberDto member = (MemberDto) session.getAttribute("user");
        String helper = member.getId();
        System.out.println(helper);

        //paging 설정
        pageRequestDTO.setSize(3);

        //페이지 설정 값 따로 만들어 주기
        PageRequestDTO pageRequestDTO2 = PageRequestDTO.builder().page(1).size(3).build();
        //경매 진행 중 onAuction=true
        if (onAuction) {
            model.addAttribute("onAuction", onAuction);
            model.addAttribute("bidsOn", auctionService.getMyBids(pageRequestDTO, helper, onAuction)); //경매 진행 중 목록
            model.addAttribute("bidsDone", auctionService.getMyBids(pageRequestDTO2, helper,false)); //경매 완료 목록
        }

        //미션 완료 리스트 onAuction=false
        else {
            model.addAttribute("onAuction", onAuction);
            model.addAttribute("matchedAuctionList", auctionService.getMyBids(pageRequestDTO2, helper,true)); //경매 진행 중 목록
            model.addAttribute("allDoneList", auctionService.getMyBids(pageRequestDTO, helper, onAuction)); //경매 완료 목록
        }

    }

    //경매 낙찰 미션 보기
    @GetMapping("/myChosenBidsHelper")
    public void MyChosenBidsHelper(PageRequestDTO pageRequestDTO, Model model, HttpSession session, boolean allDone) {

        //메인에서 user 값 받아서 리스트 출력
        //log.info("======= list ========");
        MemberDto member = (MemberDto) session.getAttribute("user");
        String helper = member.getId();
        System.out.println(helper);

        //paging 설정
        pageRequestDTO.setSize(3);

        //페이지 설정 값 따로 만들어 주기
        PageRequestDTO pageRequestDTO2 = PageRequestDTO.builder().page(1).size(3).build();
        //미션 대기 중 allDone=false
        if (!allDone) {
            model.addAttribute("allDone", allDone);
            model.addAttribute("missionWait", auctionService.getMyChosenBids(pageRequestDTO, helper, false)); //미션 대기 중 목록
            model.addAttribute("missionDone", auctionService.getMyChosenBids(pageRequestDTO2, helper,true)); //미션 완료 목록
        }

        //미션 완료 리스트 onAuction=false
        else {
            model.addAttribute("allDone", allDone);
            model.addAttribute("missionWait", auctionService.getMyBids(pageRequestDTO2, helper,false)); //미션 대기 중 목록
            model.addAttribute("missionDone", auctionService.getMyBids(pageRequestDTO, helper, true)); //미션 완료 목록
        }

    }

    //경매 참여 가능한 옥션리스트 보기
    @GetMapping("/availableAuctions")
    public void list5(PageRequestDTO pageRequestDTO, Model model) {

        // 페이지에서 값 받아오기
        String sido = "";
        String sigungu = "";
        int category = 0;

        model.addAttribute("availableAuctions", auctionService.getAvailableAuctions(sido, sigungu, category, pageRequestDTO));
    }

    //경매 참여 가능한 옥션리스트 - 검색
    @PostMapping("/availableAuctions")
    public void list5(PageRequestDTO pageRequestDTO, Model model, HttpServletRequest request) {

        // 페이지에서 값 받아오기
        String sido = request.getParameter("sido");
        String sigungu = request.getParameter("sigungu");
        int category = Integer.parseInt(request.getParameter("category"));

        model.addAttribute("availableAuctions", auctionService.getAvailableAuctions(sido, sigungu, category, pageRequestDTO));
    }

    //=================================================== 상세보기 시작 ==================================================//
    //경매상세보기 - 진행 중 경매 혹은 매칭 미완료 (그냥 출력이라 entity값으로 넘김)
    @GetMapping("/getOnAuction")
    public void getAuction1(Long aucSeq, Model model, PageRequestDTO pageRequestDTO) {

        Member user = auctionService.getAuction(aucSeq).get().getUser();

        //옥션 정보
        model.addAttribute("auction", auctionService.getAuction(aucSeq).get());

        //옥션 유저 값
        model.addAttribute("u", user);

        //타이머 정보 보내기
        model.addAttribute("time", auctionService.timer(aucSeq));

        //비딩 정보
        PageResultsDTO<BiddingListDTO, BiddingList> bidding = auctionService.getBidding(pageRequestDTO, aucSeq);
        model.addAttribute("biddingList", bidding);

        // 비딩 참여 내역이 있는지 확인
        boolean exist = false;
        if (bidding.getDtoList().size() > 0) {
            exist = true;
        }
        model.addAttribute("exist", exist);
    }

    //경매상세보기 - 매칭 완료 또는 일 수행 완료
    @GetMapping("/getAuction")
    public void getAuction2(Long aucSeq, Model model, PageRequestDTO pageRequestDTO) {
        //옥션 정보
        AuctionList auction = auctionService.getAuction(aucSeq).get();
        model.addAttribute("auction", auction);

        //낙찰 비딩 정보
        model.addAttribute("chosenBidding", auctionService.chosenBidding(aucSeq));

        //그외 정보
        /*if (auction.getState() != 3){

            boolean isAllDone = false;
        }*/
    }

    //목록에서 연결되는 버튼 처리 - 낙찰, 삭제, 채팅, 리뷰, 비즈니스카드보기

    //=================================================== 경매 등록 ==================================================//
    //경매 등록 시작
    @GetMapping("/register")
    public void regist(Model model) {

        //일 등록 시 오늘 날짜보다 이전 선택불가
        String td = auctionService.today();
        System.out.println(td);
        model.addAttribute("td", td);

    }

    @PostMapping("/register")
    public String registerPost(AuctionListDTO dto, HttpServletRequest request) {

        log.info("register " + dto.toString());
        String date = request.getParameter("doDateT");
        System.out.println(date);
        LocalDateTime dodateTime = LocalDateTime.parse(date);
        dto.setDoDateTime(dodateTime);
        //새로 추가된 엔티티번호 받아서 출력하고 싶으면 하던가,,,,
        Long aucSeq = auctionService.register(dto);
        log.info(aucSeq);

        return "redirect:/auction/onAuctionList";
    }

    //=================================================== 이미지 업로드 ==================================================//
    //이미지 업로드 관련 시작
    // 파일 업로드
    @ResponseBody
    @PostMapping(value = "/fileUpload", produces = "application/json; charset=utf8")
    public String upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("postNo") String postNo) throws Exception {

        JsonObject jsonObject = new JsonObject();
        String fileRoot = "C:\\summernote_image\\";  // 파일이 저장될 절대 경로(상대 경로로 가능, 편의상 임시로 해놓은 것)
        //Long no = Long.parseLong(postNo);
        String originalFileName = multipartFile.getOriginalFilename();    //오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));    //파일 확장자
        String savedFileName = UUID.randomUUID() + extension;    //저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();    // request받은 multipart를 저장할 수 있게끔 InputStream 생성
            FileUtils.copyInputStreamToFile(fileStream, targetFile);    //파일 저장

            jsonObject.addProperty("url", "/resources/fileupload/" + savedFileName); // contextroot + resources + 저장할 내부 폴더명
            jsonObject.addProperty("responseCode", "success");
            jsonObject.addProperty("savedFileName", savedFileName);
            jsonObject.addProperty("originalFileName", originalFileName);

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);    //저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    // 양식에 올려놓은 파일 제거
    @ResponseBody
    @PostMapping(value = "/fileDelete", produces = "application/json; charset=utf8")
    public String delete(@RequestParam("filePathName") String filePathName) { // img 태그 내에 src 부분을 String 으로 전부 넘김

        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\summernote_image\\";  // 파일이 저장된 절대 경로

        log.info(filePathName); //  => ex: /resources/fileupload/67b7876f-976a-4c66-a835-99dd4f09eb4e.jpg
        String[] filePathNameSplit = filePathName.split("/"); // "/" 를 기준으로 split

        String fileServerPath = fileRoot + filePathNameSplit[3]; // 파일의 실제 이름

        File fileInServer = new File(fileServerPath); // 파일 객체 생성

        try {
            if (fileInServer.exists()) { // 파일이 있다면,
                fileInServer.delete(); // 파일 삭제
                log.info("file has been deleted!");
            } else {
                log.info("file does not exist!");
            }
            jsonObject.addProperty("removedFileName", filePathNameSplit[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    //이미지 관련 컨트롤러 끝

}
