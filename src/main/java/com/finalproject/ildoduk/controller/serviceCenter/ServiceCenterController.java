package com.finalproject.ildoduk.controller.serviceCenter;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.auction.AuctionBiddingDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.dto.pay.TradeHistoryDTO;
import com.finalproject.ildoduk.dto.serviceCenter.CustomerAnswerDTO;
import com.finalproject.ildoduk.dto.serviceCenter.CustomerBoardDTO;
import com.finalproject.ildoduk.dto.serviceCenter.UserReportDTO;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.serviceCenter.CustomerBoard;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;
import com.finalproject.ildoduk.service.auction.service.AuctionService;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import com.finalproject.ildoduk.service.pay.service.PaymentService;
import com.finalproject.ildoduk.service.serviceCenter.service.CustomerAnswerService;
import com.finalproject.ildoduk.service.serviceCenter.service.CustomerBoardService;
import com.finalproject.ildoduk.service.serviceCenter.service.UserReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;


@RequestMapping("/serviceCenter")
@Controller
@RequiredArgsConstructor
@Log4j2
public class ServiceCenterController {


    private final MemberService memberService;
    private final PaymentService paymentService;
    private final AuctionService auctionService;
    private final CustomerBoardService customerBoardService;
    private final CustomerAnswerService customerAnswerService;
    private final UserReportService userReportService;
    private final HelperInfoService helperInfoService;


    //결제 및 거래 조회 페이지로 이동
    @GetMapping("/paymentHistory")
    public void getPaymentHistroy(HttpSession session, MemberDto dto, TradeHistoryDTO tradeHistoryDTO, PageRequestDTO pageRequestDTO, Model model){
        MemberDto id = (MemberDto)session.getAttribute("user");
        log.info(id.getId()+"결제 조회 아이디");
        //해당 계정을 통하여 결제이력 불러오기
        PageResultsDTO pageResultsDTO = paymentService.getHistory(id.getId(),pageRequestDTO);

        //값이 존재한다면 payCheck : y -> 결제 완료로 수정
        if(pageResultsDTO != null) {
           // y -> 결제완료로 바꿔야한다.
            for(int i = 0; i < pageResultsDTO.getDtoList().size();i++){
              ArrayList<PaymentDTO> payCheck = (ArrayList<PaymentDTO>) pageResultsDTO.getDtoList();

              if(payCheck.get(i).getPayCheck().equals("y")){
                  payCheck.get(i).setPayCheck("결제완료");
              } else if(payCheck.get(i).getPayCheck().equals("n")) {
                  payCheck.get(i).setPayCheck("환불완료");
              }
                //수정한 값으로 다시 세팅
                pageResultsDTO.setDtoList(payCheck);
            }
        }

        model.addAttribute("result",pageResultsDTO); //결제

    }

//------------------------  환불 관련 ---------------------------
    //환불 진행(GET)
    @GetMapping("/toRefund")
    public void toRefund(@RequestParam("pointNo") Long pointNo, Model model){

        PaymentDTO dto = paymentService.toRefund(pointNo);
        MemberDto memberDto = memberService.userIdCheck(dto.getUserId());

        model.addAttribute("result",dto);
        model.addAttribute("user", memberDto);
    }

    //환불 시작(POST)
    @PostMapping("/postRefund")
    public String postRefund(@RequestParam("pointNo") Long pointNo
                            ,@RequestParam("userID") Member member,
                           @RequestParam("totalPoint") int totalPoint){
        //여기서 해야할 일 1. 결제 이력 y -> n 으로 수정 (완료)
        //              2. 해당 사용자 넘어온 캐쉬만큼 포인트 뺴기 (완료)
        //              3. 조건 검사 -> 보유 캐쉬보다 많을 경우에만 (완료) -> 스크립트에서 실행

        PaymentDTO dto = new PaymentDTO();
        //변경할 값의 번호
        dto.setPointNo(pointNo);
        dto.setPayCheck("n");

        //결제 이력 수정
        paymentService.updatePayCheck(dto);

        //유저 정보 업데이트 (포인트 차감)
        MemberDto user = new MemberDto();
        user.setId(member.getId());
        user.setPoint(totalPoint);

        memberService.minusPoint(user);

        return "/index";
    }


//---------  문의 게시판 ------------------

    //문의 게시판으로 이동 : 문의 게시판의 경우 모든 회원의 글이 보이도록
    @GetMapping("/customerBoard")
    public void customerBoard(PageRequestDTO pageRequestDTO, Model model){
        //모든 정보를 조회

        //answerCheck -> n : 답변 대기중, y : 답변 완료
        PageResultsDTO<CustomerBoardDTO, CustomerBoard> result = customerBoardService.allContents(pageRequestDTO);

        ArrayList<CustomerBoardDTO> board = (ArrayList<CustomerBoardDTO>)result.getDtoList();

        for(int i=0;i<result.getDtoList().size();i++){
            //비공개 여부 확인
            if(board.get(i).getSecretBoard().equals("y")){
              board.get(i).setCusTitle("비공개 게시글 입니다.");
            }

            //게시글 답변 확인
            if(board.get(i).getAnswerCheck().equals("n")){
                board.get(i).setAnswerCheck("답변 대기중");
            } else if(board.get(i).getAnswerCheck().equals("y")){
                board.get(i).setAnswerCheck("답변 완료");
            }

            //게시글 작성자를 닉네임으로 나오게
            String writer = board.get(i).getCusWriter();
            MemberDto memberDto = memberService.userIdCheck(writer);
            String nick = memberDto.getNickname();
            board.get(i).setCusWriter(nick);

            result.setDtoList(board);
        }
        model.addAttribute("cusBoard",result);
    }

    //문의작성 폼으로 이동
    @GetMapping("/customerWriteForm")
    public void cusWriteForm(){
    }

    //문의글 작성
    @PostMapping("/postCusWrite")
    public String postWriteForm(CustomerBoardDTO dto){
        customerBoardService.insertCusBoard(dto);
        return "redirect:/serviceCenter/customerBoard";
    }

    //문의글 상세보기
    @PostMapping("/secretBoard")
    @ResponseBody
    public CustomerBoardDTO testBoard(@RequestBody HashMap<String,String> cusNo,Model model){

        log.info("비동기 테스트 : ~~~~~~" + cusNo.get("cusNo"));

        Long num = Long.parseLong(cusNo.get("cusNo"));

        return customerBoardService.getBoardList(num);
    }

    //아무 조건이 걸려있지 않은 문의글 상세보기
    @GetMapping("/customerGetBoard")
    public void detailBoard(CustomerBoardDTO customerBoardDTO,@ModelAttribute("requestDTO") PageRequestDTO requestDTO
            ,Model model){

        CustomerBoardDTO board = customerBoardService.getBoardList(customerBoardDTO.getCusNo());

        MemberDto memberDto = memberService.userIdCheck(board.getCusWriter());
        board.setCusWriter(memberDto.getNickname());

        model.addAttribute("board",board);
        model.addAttribute("user","check");

        CustomerAnswerDTO answerDTO = customerAnswerService.getAnswer(customerBoardDTO.getCusNo());
        //답글이 존재 할 경우
        if(answerDTO != null){
            model.addAttribute("answer",answerDTO);
        }
    }

    //관리자 상세보기 (비밀번호, 작성자와 상관없이 접근 가능)
    @GetMapping("/mgrBoard")
    public String mgrboard(CustomerBoardDTO customerBoardDTO, HttpSession session, Model model, @ModelAttribute("requestDTO") PageRequestDTO requestDTO){

        CustomerBoardDTO board = customerBoardService.getBoardList(customerBoardDTO.getCusNo());

        MemberDto memberDto = memberService.userIdCheck(board.getCusWriter());
        board.setCusWriter(memberDto.getNickname());

        model.addAttribute("board",board);
        model.addAttribute("user","check");

        CustomerAnswerDTO answerDTO = customerAnswerService.getAnswer(customerBoardDTO.getCusNo());
        //답글이 존재 할 경우
        if(answerDTO != null){
            model.addAttribute("answer",answerDTO);
        }
        return "/serviceCenter/customerGetBoard";
    }


     //문의글 (비공개글) 열기
    @PostMapping("/customerGetBoard")
    public void postGetBoard(CustomerBoardDTO dto,@ModelAttribute("requestDTO") PageRequestDTO requestDTO,Model model){

        CustomerBoardDTO customerBoardDTO = customerBoardService.getBoardList(dto.getCusNo());

        MemberDto memberDto = memberService.userIdCheck(customerBoardDTO.getCusWriter());
        customerBoardDTO.setCusWriter(memberDto.getNickname());

        model.addAttribute("board",customerBoardDTO);
        model.addAttribute("user","check");

        CustomerAnswerDTO answerDTO = customerAnswerService.getAnswer(customerBoardDTO.getCusNo());
        //답글이 존재 할 경우
        if(answerDTO != null){
            model.addAttribute("answer",answerDTO);
        }
    }

    //문의글 수정페이지 이동
    @GetMapping("/customerUpdateBoard")
    public void updateBoard(@RequestParam("cusNo") Long cusNo
            ,@ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){

        CustomerBoardDTO customerBoardDTO = customerBoardService.getBoardList(cusNo);
        MemberDto memberDto = memberService.userIdCheck(customerBoardDTO.getCusWriter());
        customerBoardDTO.setCusWriter(memberDto.getNickname());

        model.addAttribute("board",customerBoardDTO);
    }

    //게시글 수정
    @PostMapping("/update")
    public String update(CustomerBoardDTO dto){
       MemberDto memberDto = memberService.userNickCheck(dto.getCusWriter());
       dto.setCusWriter(memberDto.getId());
       customerBoardService.updateBoard(dto);

       return "redirect:/serviceCenter/customerBoard";
    }

    //게시글 삭제
    @PostMapping("/delete")
    public String delete(CustomerBoardDTO dto){
        CustomerBoardDTO result = customerBoardService.getBoardList(dto.getCusNo());
        log.info("삭제할 데이터 : "+result);
        customerBoardService.deleteBoard(result);

        return "redirect:/serviceCenter/customerBoard";
    }


//---------  문의게시글에 답변 남기기 ----------

    //답글 등록
    @ResponseBody
    @PostMapping(value = "/boardComment", produces = "application/json; charset=utf8")
    public  ResponseEntity<Long> registerComment(@RequestBody HashMap<String,String> hashMap,CustomerAnswerDTO customerAnswerDTO,CustomerBoardDTO customerBoardDTO) {

        customerAnswerDTO.setCusNo(Long.parseLong(hashMap.get("cusNo")));
        customerAnswerDTO.setAContent(hashMap.get("aContent"));
        customerAnswerDTO.setAWriter(hashMap.get("aWriter"));

        customerAnswerService.insertBoard(customerAnswerDTO);

        //답글 등록했을 경우 해당 게시물의 답변 여부 바꿔줘야한다.
        customerBoardDTO.setCusNo(Long.parseLong(hashMap.get("cusNo")));
        customerBoardDTO.setAnswerCheck("y");

        customerBoardService.updateAnswer(customerBoardDTO);

        return new ResponseEntity<>(1L, HttpStatus.OK);
    }

    // 댓글 삭제
    @ResponseBody
    @DeleteMapping(value = "/deleteComment", produces = "application/json; charset=utf8")
    public ResponseEntity<Long> deleteComment(@RequestBody String aNo, CustomerBoardDTO customerBoardDTO) {
        log.info("넘어온 해당 답글 번호 : "+ aNo);
        Long aNum = Long.parseLong(aNo);

        //마찬가지로 답글을 삭제할 경우 답변여부 다시 바꿔줌

        customerBoardDTO.setCusNo(customerAnswerService.deleteComment(aNum));
        customerBoardDTO.setAnswerCheck("n");

        customerBoardService.updateAnswer(customerBoardDTO);

        return new ResponseEntity<>(1L, HttpStatus.OK);
    }


//--------  사용자 신고 --------------

    //사용자 신고 게시판으로 이동
    @GetMapping("/badUserReport")
    public void report(HttpSession session,PageRequestDTO pageRequestDTO,Model model){

        MemberDto id = (MemberDto) session.getAttribute("user");
        UserReportDTO dto = new UserReportDTO();
        dto.setId(id.getId());
        PageResultsDTO<UserReportDTO, UserReport> list = userReportService.getReportList(dto,pageRequestDTO);

        log.info("신고 게시판 이동 시 뽑아내는 데이터 : "+list.getDtoList());

        if(list != null){

            ArrayList<UserReportDTO> userList = (ArrayList<UserReportDTO>) list.getDtoList();

            for (int i=0;i<list.getDtoList().size();i++){
                //해당 계정의 닉네임......
                 String reportTarget = userList.get(i).getReportTarget();
                 MemberDto memberDto = memberService.userIdCheck(reportTarget);

                 String nick = memberDto.getNickname();
                 list.getDtoList().get(i).setReportTarget(nick);

                 if(list.getDtoList().get(i).getReportState().equals("1")){
                     list.getDtoList().get(i).setReportState("신고 접수 완료");
                 } else if(list.getDtoList().get(i).getReportState().equals("2")) {
                     list.getDtoList().get(i).setReportState("처리 완료");
                 }
            }
        }
        model.addAttribute("reportList",list);
    }

    //신고 작성 폼으로 이동
    @GetMapping("/badUserReportForm")
    public void reportForm(HttpSession session,
                           PageRequestDTO pageRequestDTO
                           , Model model){
        //폼으로 이동할 때 세션 유저 값과 해당 신고 대상자의 아이디 필요..
        //나와 거래 했던 사람들의 정보를 넘겨줘야한다.
        MemberDto user = (MemberDto) session.getAttribute("user");

        PageResultsDTO<AuctionBiddingDTO, Object[]> list = auctionService.getList4(pageRequestDTO, user.getId());

        for(int i=0;i<list.getDtoList().size();i++) {
            String id = list.getDtoList().get(i).getHelper();

            MemberDto memberDto = memberService.userIdCheck(id);
            String nick = memberDto.getNickname();
            list.getDtoList().get(i).setHelperNickName(nick);
            
        }
        model.addAttribute("tradeList",list);


    }



    //신고 작성
    @PostMapping("/userReportWrite")
    public String writeReport(UserReportDTO reportDTO){
        //넘어오는 신고 대상 아이디는 닉네임으로 되어있다. 다시 아이디로 변환...
        String nick = reportDTO.getReportTarget();

        MemberDto memberDto = memberService.userNickCheck(nick);
        reportDTO.setReportTarget(memberDto.getId());

        userReportService.insertReport(reportDTO);

        return "redirect:/serviceCenter/badUserReport";
    }

    //신고 내용 상세 보기
    @GetMapping("/badUserReportDetail")
    public void badUserReportDetail(UserReportDTO userReportDTO,Model model) {

        UserReportDTO reportDetail = userReportService.badUserReportDetail(userReportDTO);
        MemberDto memberDto = memberService.userIdCheck(reportDetail.getReportTarget());
        //님네임으로 변환
        reportDetail.setReportTarget(memberDto.getNickname());
        // 남은 작업 : 상세보기시에 종류에 따라 해당 값 세팅, 신고 처리 상태에 따라 값 세팅
        if(reportDetail.getReportKind().equals("1")){
            reportDetail.setReportKind("광고");
        }else if(reportDetail.getReportKind().equals("2")){
            reportDetail.setReportKind("도배");
        }else if(reportDetail.getReportKind().equals("3")){
            reportDetail.setReportKind("불법 음란물 게시");
        }else if(reportDetail.getReportKind().equals("4")){
            reportDetail.setReportKind("욕설 및 폭언");
        }else if(reportDetail.getReportKind().equals("5")){
            reportDetail.setReportKind("저작권 침해");
        }else if(reportDetail.getReportKind().equals("6")){
            reportDetail.setReportKind("개인정보 침해");
        } else {
            reportDetail.setReportKind("기타");
        }


        model.addAttribute("reportDetail",reportDetail);
    }
    //신고 삭제
    @PostMapping("/reportDelete")
    public String reportDelete(UserReportDTO userReportDTO){

        MemberDto memberDto = memberService.userNickCheck(userReportDTO.getReportTarget());

        String id = memberDto.getId();
        userReportDTO.setReportTarget(id);

        userReportService.reportDelete(userReportDTO);

        return "redirect:/serviceCenter/badUserReport";
    }

    //관리자로 접속시에 신고 상황 업데이트
    @GetMapping("/reportBoardMgr")
    public String reportBoardMgr(PageRequestDTO pageRequestDTO,Model model){
        PageResultsDTO<UserReportDTO, UserReport> reportStateOne = userReportService.getStateOne(pageRequestDTO);
        PageResultsDTO<UserReportDTO, UserReport> reportStateTwo = userReportService.getStateTwo(pageRequestDTO);

        model.addAttribute("reportStateOne",reportStateOne);
        model.addAttribute("reportStateTwo",reportStateTwo);

        return "/manager/badUserReportMgr";
    }

    @GetMapping("/reportStateUpdate")
    public String reportStateUpdate(Long reportNo,UserReportDTO userReportDTO){
        log.info("관리자 : 신고 버튼");
        //신고 상황 처리
        userReportDTO.setReportNo(reportNo);
        userReportDTO.setReportState("2");

        userReportService.updateReportState(userReportDTO);

        //신고 당한 유저 -> kindness를 깍아야하나..



        return "redirect:/serviceCenter/reportBoardMgr";
    }

//------ 유저  <-> 헬퍼 전환 버튼
    @GetMapping("/changeState")
    public String changeState(HttpSession session){
        //먼저 helperInfo의 agreeHelper를 체크해서 2일 경우에만 해당 로직 실행?? 버튼
        String id = (String)session.getAttribute("user");
        HelperInfoDTO helperInfoDTO = new HelperInfoDTO();

        helperInfoDTO.setMemberId(id);
        HelperInfoDTO info = helperInfoService.helperInfo(helperInfoDTO);

        MemberDto memberDto = memberService.userIdCheck(id);

        if(info.getAgreeHelper() == 2){
            //member state 가 1 일 경우 -> 2로
            if(memberDto.getState() == 1){
                memberDto.setState(2);
                memberService.updateState(memberDto);
            } else {
                //2일 경우 -> 1로
                memberDto.setState(1);
                memberService.updateState(memberDto);
            }
        }

        return "/index";
    }

//-----------  FAQ ------------------------
    //사용자 FAQ
    @GetMapping("/faq")
    public void faq(){

    }

    //관리자 FAQ
    @GetMapping("/faqMgr")
    public void faqMgr(){

    }




}
