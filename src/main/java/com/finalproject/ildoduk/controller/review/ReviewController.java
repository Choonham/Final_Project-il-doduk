package com.finalproject.ildoduk.controller.review;


import com.finalproject.ildoduk.dto.review.BidDTO;
import com.finalproject.ildoduk.service.bidding.service.BiddingService;
import com.finalproject.ildoduk.service.review.ServiceInterface.ReviewService;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
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


    /*=======단순히 리스트만=====*/
    @GetMapping("/reviewList")
    public void reviewList(Model model){


    model.addAttribute("list",service.getList());


    }
    @GetMapping("/writeform")
    public void writeform(Model model , @RequestParam("bid") String bid){
        BidDTO dto =bidservice.get_bidding(bid);
        String helperID=dto.getHelper().getId();


    }
    @PostMapping("/write")
    public String write(@RequestParam("editordata") String content){

        System.out.println(content);
        return "/review/reviewList";
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
