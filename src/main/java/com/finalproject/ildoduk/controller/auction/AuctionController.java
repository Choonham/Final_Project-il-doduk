package com.finalproject.ildoduk.controller.auction;

import com.finalproject.ildoduk.dto.*;
import com.finalproject.ildoduk.dto.auction.*;
import com.finalproject.ildoduk.dto.member.*;
import com.finalproject.ildoduk.entity.auction.*;
import com.finalproject.ildoduk.entity.member.*;
import com.finalproject.ildoduk.service.auction.service.*;
import com.finalproject.ildoduk.service.member.service.*;
import com.finalproject.ildoduk.service.pay.service.*;
import com.google.gson.*;
import lombok.*;
import lombok.extern.log4j.*;
import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.*;
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

    @Autowired
    private final HelperInfoService helperInfoService;

    @Autowired
    private final PaymentService paymentService;

    //네비바 정리하고 없어질 컨트롤러~~
    @GetMapping("/main")
    public void main(PageRequestDTO pageRequestDTO) {
        //경매 리스트 시간에 따라 상태값 변화 하기
        auctionService.changeState1(pageRequestDTO); //경매 시간 끝난 것 state=1로 변경
        auctionService.changeState2(pageRequestDTO); //미션 시작 시간(-30분) 초과하고, 매칭이 안된 것 state=4로 변경
    }

    //접근권한 없는 member가 접근 시 인덱스로 반환 - 필요 시 사용
    @GetMapping("/index")
    public String index() {
        System.out.println("index함수 실행");
        return "redirect:/index";
    }

    //=================================================== User 리스트 ==================================================//
    //auc.state=0 경매 진행 중 / auc.state=1 경매완료, 매칭미완료 / auc.state=2 매칭완료 / auc.state=3 일 수행 완료 / auc.state=4 삭제

    //경매 진행 중 미션, 경매 완료된 미매칭 미션 페이지
    @GetMapping("/onAuctionList")
    public void list1(PageRequestDTO pageRequestDTO, Model model, HttpSession session, boolean isAuctionDone) {

        //경매 리스트 시간에 따라 상태값 변화 하기
        auctionService.changeState1(pageRequestDTO); //경매 시간 끝난 것 state=1로 변경
        auctionService.changeState2(pageRequestDTO); //미션 시작 시간(-30분) 초과하고, 매칭이 안된 것 state=4로 변경

        //member 정보 얻기
        MemberDto member = (MemberDto) session.getAttribute("user");
        String user = member.getId();

        System.out.println("==============="+ member.getState());

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
    public void list3(PageRequestDTO pageRequestDTO, Model model, HttpSession session, boolean isAllDone) {

        MemberDto member = (MemberDto) session.getAttribute("user");
        String user = member.getId();

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

        //경매 리스트 시간에 따라 상태값 변화 하기
        auctionService.changeState1(pageRequestDTO); //경매 시간 끝난 것 state=1로 변경
        auctionService.changeState2(pageRequestDTO); //미션 시작 시간(-30분) 초과하고, 매칭이 안된 것 state=4로 변경

        //메인에서 user 값 받아서 리스트 출력
        MemberDto member = (MemberDto) session.getAttribute("user");
        String helper = member.getId();

        //paging 설정
        pageRequestDTO.setSize(3);

        //페이지 설정 값 따로 만들어 주기
        PageRequestDTO pageRequestDTO2 = PageRequestDTO.builder().page(1).size(3).build();
        //경매 진행 중 onAuction=true
        if (onAuction) {
            model.addAttribute("onAuction", onAuction);
            model.addAttribute("bidsOn", auctionService.getMyBids(pageRequestDTO, helper, onAuction)); //경매 진행 중 목록
            model.addAttribute("bidsDone", auctionService.getMyBids(pageRequestDTO2, helper, false)); //경매 완료 목록
        }

        //미션 완료 리스트 onAuction=false
        else {
            model.addAttribute("onAuction", onAuction);
            model.addAttribute("bidsOn", auctionService.getMyBids(pageRequestDTO2, helper, true)); //경매 진행 중 목록
            model.addAttribute("bidsDone", auctionService.getMyBids(pageRequestDTO, helper, onAuction)); //경매 완료 목록
        }

    }

    //경매 낙찰 미션 보기
    @GetMapping("/myChosenBidsHelper")
    public void MyChosenBidsHelper(PageRequestDTO pageRequestDTO, Model model, HttpSession session, boolean allDone) {

        //메인에서 user 값 받아서 리스트 출력
        MemberDto member = (MemberDto) session.getAttribute("user");
        String helper = member.getId();

        //paging 설정
        pageRequestDTO.setSize(3);

        //페이지 설정 값 따로 만들어 주기
        PageRequestDTO pageRequestDTO2 = PageRequestDTO.builder().page(1).size(3).build();

        //미션 대기 중 allDone=false
        if (!allDone) {
            model.addAttribute("allDone", allDone);
            model.addAttribute("missionWait", auctionService.getMyChosenBids(pageRequestDTO, helper, false)); //미션 대기 중 목록
            model.addAttribute("missionDone", auctionService.getMyChosenBids(pageRequestDTO2, helper, true)); //미션 완료 목록
        }

        //미션 완료 리스트 onAuction=false
        else {
            model.addAttribute("allDone", allDone);
            model.addAttribute("missionWait", auctionService.getMyBids(pageRequestDTO2, helper, false)); //미션 대기 중 목록
            model.addAttribute("missionDone", auctionService.getMyBids(pageRequestDTO, helper, true)); //미션 완료 목록
        }

    }

    //경매 참여 가능한 옥션리스트 보기
    @GetMapping("/availableAuctions")
    public void list5(PageRequestDTO pageRequestDTO, Model model) {

        //경매 리스트 시간에 따라 상태값 변화 하기
        auctionService.changeState1(pageRequestDTO); //경매 시간 끝난 것 state=1로 변경
        auctionService.changeState2(pageRequestDTO); //미션 시작 시간(-30분) 초과하고, 매칭이 안된 것 state=4로 변경

        // 페이지에서 값 받아오기
        String sido = "";
        String sigungu = "";
        int category = 0;

        model.addAttribute("availableAuctions", auctionService.getAvailableAuctions(sido, sigungu, category, pageRequestDTO));
    }

    //경매 참여 가능한 옥션리스트 - 검색
    @PostMapping("/availableAuctions")
    public void list5(PageRequestDTO pageRequestDTO, Model model, HttpServletRequest request) {

        //경매 리스트 시간에 따라 상태값 변화 하기
        auctionService.changeState1(pageRequestDTO); //경매 시간 끝난 것 state=1로 변경
        auctionService.changeState2(pageRequestDTO); //미션 시작 시간(-30분) 초과하고, 매칭이 안된 것 state=4로 변경

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

        String userId = auctionService.getAuction(aucSeq).getUser();
        Member user = auctionService.getMember(userId);
        //옥션 정보
        AuctionListDTO auctionList = auctionService.getAuction(aucSeq);
        model.addAttribute("auction", auctionList);

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

        //==========경매차액 리스트 생성 및 보내기=========//
        int startPrice = auctionList.getStartPrice();
        int auctionGap = auctionList.getAuctionGap();

        //차액리스트 갯수
        int n = ((startPrice - 5000) / auctionGap);

        //차액리스트 생성
        ArrayList<Integer> gaps = new ArrayList<Integer>();
        //리스트에 값 넣기
        for(int i=0; i<n; i++){
            int gap = i*auctionGap;
            gaps.add(gap);
        }

        //차액리스트 전달
        model.addAttribute("gaps",gaps);
    }

    //경매상세보기 - 매칭 완료 또는 일 수행 완료
    @GetMapping("/getAuction")
    public void getAuction2(Long aucSeq, Model model) {

        //System.out.println("aucSe======="+aucSeq);
        String userid = auctionService.getAuction(aucSeq).getUser();
        Member user = auctionService.getMember(userid);

        //옥션 정보
        AuctionListDTO auction = auctionService.getAuction(aucSeq);
        model.addAttribute("auction", auction);

        //옥션 유저 값
        model.addAttribute("u", user);

        if(auction.getState() != 4) {
            //낙찰 비딩 정보
            BiddingList chosenBidding = auctionService.chosenBidding(aucSeq).get();
            //낙찰 된 헬퍼 정보 (멤버+헬퍼인포)
            String helperId = chosenBidding.getHelper().getId();

            if (chosenBidding != null && helperId != null) {
                model.addAttribute("chosenBidding", chosenBidding);
                model.addAttribute("helper", helperInfoService.helperFindById2(helperId));
            }
        }
    }

    //목록에서 연결되는 버튼 처리 - 채팅, 리뷰, 비즈니스카드보기

    @GetMapping("/choose")
    public void choose(Long bidSeq, Model model) {

        //비딩정보 보내기
        BiddingList biddingList = auctionService.getOneBid(bidSeq).get();

        //해당 헬퍼정보 보내기
        MemberHelperInfoDTO helper = helperInfoService.helperFindById2(biddingList.getHelper().getId());

        if (biddingList != null && helper != null) {
            model.addAttribute("bid", biddingList);
            model.addAttribute("helper", helper);
        }

    }

    //낙찰 진행
    @GetMapping("/chooseBid")
    public void chooseBid(Long bidSeq) {

        //System.out.println("controller============="+bidSeq);

        auctionService.chooseBidding(bidSeq);

        /*결제관련*/
        //낙찰 후 차액 반환
        paymentService.biddingSuccess(bidSeq);

        /*낙찰 된 헬퍼에게 문자 메세지 보내기*/
        BiddingList bid = auctionService.getOneBid(bidSeq).get();
        String helperPhone = bid.getHelper().getPhone();
        String user = bid.getAucSeq().getUser().getNickname();
        String text = "안녕하세요, 헤르메스입니다. " + user + "님의 미션에 낙찰되셨습니다!";
        //auctionService.sendSMS(helperPhone,text);

        /*낙찰 된 헬퍼에게 메일 보내기 -> 메일주소가 유니크키여서 테스트를 위해 우선 하드코드로 지정*/
        String userEmail = bid.getHelper().getId();
        //String userEmail = "godnjs729417@naver.com";
        String title = "헤르메스입니다.";
        auctionService.sendMail(userEmail, title, text);
        //받는주소와 메일 내용 인자로 전달
    }

    //경매참여
    @PostMapping("/biddingIn")
    public String biddinIn2(BiddingListDTO dto) {

        auctionService.biddingIn(dto);

        return "redirect:/auction/getOnAuction?aucSeq=" + dto.getAucSeq();
    }

    //경매삭제
    @GetMapping("/deleteAuction")
    public String deleteAuction(Long aucSeq) {

        //경매와 해당 경매 비딩 내역 state값 변경
        //auction.state=4 / bids.state=2 (삭제)
        auctionService.deleteAuction(aucSeq);

        /*결제 관련*/
        paymentService.refundAuctionPay(aucSeq);

        return "redirect:/auction/onAuctionList";
    }

    //미션 수행 확인 (미션종료 상태로 state 변환)
    @GetMapping("/jobDone")
    public String jobDone(Long aucSeq) {

        //경매와 해당 경매 비딩 내역 state값 변경
        //auction.state=3(일 완료)
        auctionService.jobDone(aucSeq);

        /*결제관련*/
        paymentService.doneAuction(aucSeq);

        return "redirect:/auction/getAuction?aucSeq=" + aucSeq;
    }

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

        String date = request.getParameter("doDateT");
        LocalDateTime dodateTime = LocalDateTime.parse(date);
        dto.setDoDateTime(dodateTime);

        //새로 추가된 엔티티번호 받아서 출력하고 싶으면 하던가,,,,
        Long aucSeq = auctionService.register(dto);
        //log.info(aucSeq);

        /*결제 관련*/
        paymentService.regAuction(aucSeq);

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
