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
import com.finalproject.ildoduk.entity.serviceCenter.CustomerBoard;
import com.finalproject.ildoduk.service.member.service.MemberService;
import com.finalproject.ildoduk.service.pay.PaymentService;
import com.finalproject.ildoduk.service.pay.TradeService;
import com.finalproject.ildoduk.service.serviceCenter.CustomerAnswerService;
import com.finalproject.ildoduk.service.serviceCenter.CustomerBoardService;
import com.finalproject.ildoduk.service.serviceCenter.UserReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    //결제 이력 페이지로 이동
    @GetMapping("/paymentHistory")
    public void getPaymentHistroy(@RequestParam("member") String id, MemberDto dto, TradeHistoryDTO tradeHistoryDTO, PageRequestDTO pageRequestDTO, Model model){

        //해당 계정을 통하여 결제이력 불러오기
        PageResultsDTO pageResultsDTO = paymentService.getHistory(id,pageRequestDTO);

        //거래 내역 조회
        tradeHistoryDTO.setId(id);
        PageResultsDTO pageResultsDTO_trade = tradeService.allContents(tradeHistoryDTO,pageRequestDTO);

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
        //환불창을 열때 해당 금액보다 유저의 포인트가 없을 경우 다시 리스트로 돌아가서 경고창..

        int point = dto.getTotalPoint();

        model.addAttribute("result",dto);
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

        return "/main";
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
        //넘어오는 데이터 : 제목, 내용, 작성자, 비밀글 여부, ( 비밀글일시 비밀번호)

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

        //추가 되야할 부분 => 관리자페이지를 따로 만들지 않을려면 이쪽에서처리 : 관리자는 모든 글을 읽을 수 있다.

        //관리자의 답글이 존재할 경우 같이 값에 담아서 전달해야함..

        if(dto.getCusWriter().equals("admin")){
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

        model.addAttribute("board",customerBoardService.getBoardList(cusNo));
    }

//게시글 수정
    @PostMapping("/update")
    public String update(CustomerBoardDTO dto){

       customerBoardService.updateBoard(dto);

       return "redirect:/serviceCenter/customerBoard";
    }

    //게시글 삭제
    @PostMapping("/delete")
    public String delete(CustomerBoardDTO dto){
        CustomerBoardDTO result = customerBoardService.getBoardList(dto.getCusNo());
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

    // 댓글 수정
    @ResponseBody
    @DeleteMapping(value = "/modifyComment", produces = "application/json; charset=utf8")
    public ResponseEntity<Long> modifyComment(@RequestBody Long aNo) {
        log.info("넘어온 데이터 : " + aNo);
        // blogCommentService.modifyComment(customerAnswerDTO);
        return new ResponseEntity<>(1L, HttpStatus.OK);
    }

//사용자 신고 게시판으로 이동
    @GetMapping("/badUserReport")
    public void report(String userId){
        //현재 신고 현황..??
        //신고 내역이 있을 경우 신고 내역을 보여주고, 없을 경우에 신고 작성 페이지로..

    }

    //신고 작성 폼으로 이동
    @GetMapping("/badUserReportForm")
    public void reportForm(){
        //폼으로 이동할 때 세션 유저 값과 해당 신고 대상자의 아이디 필요..
    }

    //신고 작성
    @PostMapping("/userReportWrite")
    public String writeReport(UserReportDTO reportDTO){
        userReportService.insertReport(reportDTO);
        return "redirect:/serviceCenter/badUserReport";
    }


}
