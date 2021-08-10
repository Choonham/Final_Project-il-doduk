package com.finalproject.ildoduk.controller.review;


import com.finalproject.ildoduk.dto.auction.AuctionListDTO;
import com.finalproject.ildoduk.dto.auction.BiddingListDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.review.RequestDto;
import com.finalproject.ildoduk.dto.review.ResultDto;
import com.finalproject.ildoduk.dto.review.ReviewDTO;
import com.finalproject.ildoduk.dto.review.UpdateDTO;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.review.Review;
import com.finalproject.ildoduk.service.auction.service.AuctionService;
import com.finalproject.ildoduk.service.bidding.service.BiddingService;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import com.finalproject.ildoduk.service.review.ServiceInterface.ReviewService;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    ReviewService service;
    @Autowired
    BiddingService bidservice;
    @Autowired
    AuctionService auctionService;
    @Autowired
    HelperInfoService infoService;
    @Autowired
    MemberService memberService;
    /*=======단순히 리스트만=====*/
    @GetMapping("/reviewList")
    public void reviewList(RequestDto dto, Model model){

        ResultDto<ReviewDTO,Review> res = service.getList(dto);
        List<ReviewDTO> list=res.getDtoList();
        System.out.println(list.get(1).getContent());
    model.addAttribute("result",service.getList(dto));


    }

    // wrtie form 으로 가는 메소드 이 model에 생각보다 많은 정보가 들어있는데 이는 후기작성의 틀을 마련 해 준다.
    @GetMapping("/writeform")
    public void writeform(Model model , @RequestParam("bid") String bid, HttpServletRequest request){
      Long long_bid = Long.parseLong(bid);
        BiddingListDTO dto =bidservice.get_bidding(long_bid);
        String helperID=dto.getHelper();
        Long aucSeq = dto.getAucSeq();
        HttpSession session = request.getSession();
        session.setAttribute("bidNo",bid );
        HelperInfoDTO infoDTO=infoService.helperFindById(dto.getHelper());
        AuctionListDTO auctionListDTO=auctionService.findAuction(aucSeq);
        System.out.println(auctionListDTO.getTitle());
        if(dto!=null&&infoDTO!=null&&auctionListDTO!=null) {
            model.addAttribute("helper",infoDTO );
            model.addAttribute("bid", dto);
            model.addAttribute("auction",auctionListDTO );

        }
    }

    //글쓰는 컨트롤러
    @PostMapping("/write")
    public String write(@RequestParam("editordata") String content, @RequestParam("title") String title, HttpServletRequest request, RedirectAttributes  redirect){
        HttpSession session = request.getSession();
        String bidStr=(String)session.getAttribute("bidNo");

        Long bidNo=Long.parseLong(bidStr);

        MemberDto member=(MemberDto)session.getAttribute("user");
       ReviewDTO dto =ReviewDTO.builder().content(content).title(title).id(member.getId()).bidSeq(bidNo).build();
       service.writeReview(dto);

       redirect.addAttribute("title",title);
        return "redirect:/review/detail";
    }

    //다테일 리스트에서 no로// 작성 후 상세 보기에서는 제목으로 검색한다.

    @GetMapping("/detail")
    public void detail(Model model,@RequestParam(value = "no",required = false)String No , @RequestParam(value = "title", required = false) String title){
        System.out.println("타이틀  :" +title);

        if(title!=null){
            ReviewDTO dto=service.get_ReviewByTitle(title);
            System.out.println("뭐고"+dto.getBidSeq());
             model.addAttribute("review",dto);
             model.addAttribute("bid", bidservice.get_bidding(dto.getBidSeq()));
        }else {

            ReviewDTO dto=service.get_reviewbyNo(Long.parseLong(No));
            Long no=Long.parseLong(No);
            model.addAttribute("review",service.get_reviewbyNo(no));
            model.addAttribute("bid", bidservice.get_bidding(dto.getBidSeq()));
        }

    }

@GetMapping("/updateform")
public void updateform(Model model,@RequestParam("no") String no){

    Long Lno = Long.parseLong(no);
    ReviewDTO dto =service.get_reviewdtobyprimary(Lno);

    model.addAttribute("review",dto);


    }
    @PostMapping("/update")
public String update(  UpdateDTO dto, RedirectAttributes redirect){


        ReviewDTO dto1=service.get_reviewdtobyprimary(Long.parseLong(dto.getNo()));
            dto1.setContent(dto.getEditordata());
            dto1.setTitle(dto.getTitle());
        service.writeReview(dto1);
        redirect.addAttribute("title",dto.getTitle());

        return "redirect:/review/detail";

    }

    @GetMapping("/delete")
public String delete(@RequestParam("no") String no){


        service.delete(no);

        return "redirect:/review/reviewList";
    }



//responsebody로 받기가 가능해졌다.


    @RequestMapping(value="/uploadSummernoteImageFile", produces = "application/json; charset=utf8")
    @ResponseBody
    public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request )  {
        JsonObject jsonObject = new JsonObject();


        // 내부경로로 저장

      String fileRoot = "D:\\summernote_image\\";

        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명
        System.out.println(savedFileName);
        File targetFile = new File(fileRoot + savedFileName);
        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            jsonObject.addProperty("url", "/resources/fileupload/"+savedFileName); // contextroot + resources + 저장할 내부 폴더명
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }
        String a = jsonObject.toString();
        System.out.println(a);
        return a;
    }



}
