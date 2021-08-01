package com.finalproject.ildoduk.controller.serviceCenter;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.dto.pay.TradeHistoryDTO;
import com.finalproject.ildoduk.dto.serviceCenter.CustomerAnswerDTO;
import com.finalproject.ildoduk.dto.serviceCenter.CustomerBoardDTO;
import com.finalproject.ildoduk.dto.serviceCenter.UserReportDTO;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.pay.TradeHistory;
import com.finalproject.ildoduk.entity.serviceCenter.CustomerBoard;
import com.finalproject.ildoduk.entity.serviceCenter.UserReport;
import com.finalproject.ildoduk.service.member.service.MemberService;
import com.finalproject.ildoduk.service.pay.service.PaymentService;
import com.finalproject.ildoduk.service.pay.service.TradeService;
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
    private final TradeService tradeService;
    private final CustomerBoardService customerBoardService;
    private final CustomerAnswerService customerAnswerService;
    private final UserReportService userReportService;


    //결제 및 거래 조회 페이지로 이동
    @GetMapping("/paymentHistory")
    public void getPaymentHistroy(HttpSession session, MemberDto dto, TradeHistoryDTO tradeHistoryDTO, PageRequestDTO pageRequestDTO, Model model){
        MemberDto id = (MemberDto)session.getAttribute("user");
        //해당 계정을 통하여 결제이력 불러오기
        PageResultsDTO pageResultsDTO = paymentService.getHistory(id.getId(),pageRequestDTO);

        //거래 내역 조회
        tradeHistoryDTO.setId(id.getId());
        PageResultsDTO pageResultsDTO_trade = tradeService.allContents(tradeHistoryDTO,pageRequestDTO);

        if(pageResultsDTO_trade != null){

            ArrayList<TradeHistoryDTO> tradeDTO = (ArrayList<TradeHistoryDTO>) pageResultsDTO_trade.getDtoList();

            for(int i=0;i<pageResultsDTO_trade.getDtoList().size();i++){

                String userId = tradeDTO.get(i).getUserId();
                //닉네임 꺼내기 위함
                MemberDto memberDto = memberService.userIdCheck(userId);
                tradeDTO.get(i).setUserId(memberDto.getNickname());

                //거래 상황 업데이트
                if(tradeDTO.get(i).getAucState().equals("1")){
                    tradeDTO.get(i).setAucState("경매 완료");
                } else if(tradeDTO.get(i).getAucState().equals("2")){
                    tradeDTO.get(i).setAucState("경매 진행중");
                } else {
                    tradeDTO.get(i).setAucState("경매 취소");
                }

                pageResultsDTO_trade.setDtoList(tradeDTO);
            }

        }

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
        model.addAttribute("trade",pageResultsDTO_trade);//거래
    }

//------------------------  환불 관련 ---------------------------
    //환불 진행(GET)
    @GetMapping("/toRefund")
    public void toRefund(@RequestParam("pointNo") Long pointNo, Model model){

        PaymentDTO dto = paymentService.toRefund(pointNo);
        //해당 유저의 총포인트 값을 같이 넘겨준다
        MemberDto memberDto = memberService.userIdCheck(dto.getUserId());

        int point = dto.getTotalPoint();

        model.addAttribute("result",dto);
        model.addAttribute("userPoint", memberDto.getPoint());
    }

    //환불 시작(POST)
    @PostMapping("/postRefund")
    public String postRefund(@RequestParam("pointNo") Long pointNo
                            ,@RequestParam("userID") Member member,
                           @RequestParam("totalPoint") int totalPoint){
        //여기서 해야할 일 1. 결제 이력 y -> n 으로 수정 (완료)
        //              2. 해당 사용자 넘어온 캐쉬만큼 포인트 뺴기 (완료)
        //              3. 조건 검사 -> 보유 캐쉬보다 많을 경우에만 아래 로직 실행

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

        memberService.minusPonit(user);

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
        //넘어오는 데이터 : 제목, 내용, 작성자, 비밀글 여부, (비밀글일시 비밀번호)

        customerBoardService.insertCusBoard(dto);
        return "redirect:/serviceCenter/customerBoard";
    }


//문의글 상세 보기
    @GetMapping("/customerGetBoard")
    public String getBoard(CustomerBoardDTO dto
                        ,@ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model,RedirectAttributes redirectAttributes){
        //이 부분에서 에러
        //해당 게시글을 눌렀을 경우 번호를 통하여 답글을 불러와야한다.
        CustomerAnswerDTO answerDTO = customerAnswerService.getAnswer(dto.getCusNo());
        //답글이 존재 할 경우
       if(answerDTO != null){
            model.addAttribute("answer",answerDTO);
        }
        CustomerBoardDTO customerBoardDTO = customerBoardService.getBoardList(dto.getCusNo());


        //접속 아이디가 관리자일 경우 모든 글 읽기 가능
        MemberDto memberDto = memberService.userIdCheck(dto.getCusWriter());
        //접속한 계정의 닉네임
        String nickName = memberDto.getNickname();
        customerBoardDTO.setCusWriter(nickName);

        if(memberDto.getState() == 0){

            model.addAttribute("board",customerBoardDTO);
            model.addAttribute("user","check");
            return "/serviceCenter/customerGetBoard";
        }

        // 다음 조건 저 글의 작성자와 넘어온 작성자가 일치하는지 조회
        String boardWriter = customerBoardDTO.getCusWriter();

        model.addAttribute("board",customerBoardDTO);

        if(dto.getCusWriter().equals(boardWriter)){
            if(customerBoardDTO.getSecretBoard().equals("y")){
                redirectAttributes.addFlashAttribute("password",customerBoardDTO.getPasswordBoard());
                redirectAttributes.addFlashAttribute("pwNo",customerBoardDTO.getCusNo());
                return "redirect:/serviceCenter/customerBoard";
             }
           model.addAttribute("user",customerBoardDTO);
        } else {
            //게시글 계정과 로그인 계정이 다른데 그 글이 비공개로 되어있다.그러면 해당 계정을 넘겨주면 되나..
            if(customerBoardDTO.getSecretBoard().equals("y")){
                redirectAttributes.addFlashAttribute("noOpen",customerBoardDTO.getCusWriter());
                return "redirect:/serviceCenter/customerBoard";
            }
            model.addAttribute("otherUser",customerBoardDTO);
        }



        return "/serviceCenter/customerGetBoard";
    }


 //문의글 (비공개글) 열기
    @PostMapping("/customerGetBoard")
    public void postGetBoard(CustomerBoardDTO dto,@ModelAttribute("requestDTO") PageRequestDTO requestDTO,Model model){
        log.info("비밀번호 체크하기 위한 게시글 번호" + dto.getCusNo());
        model.addAttribute("board",customerBoardService.getBoardList(dto.getCusNo()));
        model.addAttribute("user","check");

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
        MemberDto id = (MemberDto) session.getAttribute("user");
        log.info("신고하는 계정 : " + id.getId());
        //거래했던 유저 목록 조회
        TradeHistoryDTO tradeList = new TradeHistoryDTO();

        tradeList.setId(id.getId());
        PageResultsDTO<TradeHistoryDTO, TradeHistory> list = tradeService.allContents(tradeList, pageRequestDTO);

        if(list != null){
            for(int i=0;i<list.getDtoList().size();i++){
                //닉네임으로 변환
                MemberDto memberDto = memberService.userIdCheck(list.getDtoList().get(i).getUserId());
                log.info(memberDto.getNickname() + i);
                list.getDtoList().get(i).setUserId(memberDto.getNickname());
            }
            model.addAttribute("tradeList",list);
        }

    }

    //신고 작성
    @PostMapping("/userReportWrite")
    public String writeReport(UserReportDTO reportDTO){
        //넘어오는 신고 대상 아이디는 닉네임으로 되어있다. 다시 아이디로 변환...
        String nick = reportDTO.getReportTarget();
        log.info(nick + "신고 작성");
        MemberDto memberDto = memberService.userNickCheck(nick);
        log.info("신고 작성을 위해 가져온 정보 : "+memberDto);
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



}
