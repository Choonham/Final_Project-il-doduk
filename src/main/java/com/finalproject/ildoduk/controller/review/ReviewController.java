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
    /*=======????????? ????????????=====*/
    @GetMapping("/reviewList")
    public void reviewList(RequestDto dto, Model model, @RequestParam("id") String id){

        //model.addAttribute("list",service.getList());
        model.addAttribute("id",id);
        model.addAttribute("result",service.getList(dto,id));

    }

    // wrtie form ?????? ?????? ????????? ??? model??? ???????????? ?????? ????????? ??????????????? ?????? ??????????????? ?????? ?????? ??? ??????.
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

    //????????? ????????????
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

    //????????? ??????????????? no???// ?????? ??? ?????? ??????????????? ???????????? ????????????.

    @GetMapping("/detail")
    public void detail(Model model,@RequestParam(value = "no",required = false)String No , @RequestParam(value = "title", required = false) String title){
        System.out.println("?????????  :" +title);

        if(title!=null){
            ReviewDTO dto=service.get_ReviewByTitle(title);
            System.out.println("??????"+dto.getBidSeq());
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



//responsebody??? ????????? ???????????????.


    @RequestMapping(value="/uploadSummernoteImageFile", produces = "application/json; charset=utf8")
    @ResponseBody
    public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request )  {
        JsonObject jsonObject = new JsonObject();


        // ??????????????? ??????

      String fileRoot = "D:\\summernote_image\\";

        String originalFileName = multipartFile.getOriginalFilename();	//???????????? ?????????
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//?????? ?????????
        System.out.println("???????????? ??????????????????");
        String savedFileName = UUID.randomUUID() + extension;	//????????? ?????? ???
        System.out.println(savedFileName);
        File targetFile = new File(fileRoot + savedFileName);
        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//?????? ??????
            jsonObject.addProperty("url", "/resources/fileupload/"+savedFileName); // contextroot + resources + ????????? ?????? ?????????
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//????????? ?????? ??????
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }
        String a = jsonObject.toString();
        System.out.println("?????????");
        return a;
    }



}
