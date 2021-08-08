package com.finalproject.ildoduk.controller.review;


import com.finalproject.ildoduk.dto.auction.AuctionListDTO;
import com.finalproject.ildoduk.dto.auction.BiddingListDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.service.auction.service.AuctionService;
import com.finalproject.ildoduk.service.bidding.service.BiddingService;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import com.finalproject.ildoduk.service.review.ServiceInterface.ReviewService;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    public void reviewList(Model model){


    model.addAttribute("list",service.getList());


    }
    @GetMapping("/writeform")
    public void writeform(Model model , @RequestParam("bid") String bid){
      BiddingListDTO dto =bidservice.get_bidding(bid);
        String helperID=dto.getHelper();
        Long aucSeq = dto.getAucSeq();

        HelperInfoDTO infoDTO=infoService.helperFindById(dto.getHelper());
        AuctionListDTO auctionListDTO=auctionService.findAuction(aucSeq);
        System.out.println(auctionListDTO.getTitle());
        if(dto!=null&&infoDTO!=null&&auctionListDTO!=null) {
            model.addAttribute("helper",infoDTO );
            model.addAttribute("bid", dto);
            model.addAttribute("auction",auctionListDTO );
        }
    }


    @PostMapping("/write")
    public void write(@RequestParam("editordata") String content,@RequestParam("title") String title){

        System.out.println(content);

    }

//responsebody로 받기가 가능해졌다.


    @RequestMapping(value="/uploadSummernoteImageFile", produces = "application/json; charset=utf8")
    @ResponseBody
    public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request )  {
        JsonObject jsonObject = new JsonObject();

        /*
         * String fileRoot = "C:\\summernote_image\\"; // 외부경로로 저장을 희망할때.
         */

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
