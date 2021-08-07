package com.finalproject.ildoduk.controller.blog;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.auction.AuctionBiddingDTO;
import com.finalproject.ildoduk.dto.blog.*;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.entity.blog.BlogComment;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.service.auction.service.AuctionService;
import com.finalproject.ildoduk.service.blog.service.BlogCommentService;
import com.finalproject.ildoduk.service.blog.service.BlogFilesService;
import com.finalproject.ildoduk.service.blog.service.BlogLikeService;
import com.finalproject.ildoduk.service.blog.service.BlogService;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import com.finalproject.ildoduk.service.member.service.MemberService;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.*;

@Controller
@RequestMapping("/blog")
@Log4j2
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final BlogCommentService blogCommentService;
    private final BlogLikeService blogLikeService;
    private final BlogFilesService blogFilesService;
    private final AuctionService auctionService;
    private final HelperInfoService helperInfoService;
    private final MemberService memberService;

    //=================================== 메인 관련 시작===================================//

    // "/" 요청에 대한 처리
    @GetMapping("/")
    public String main(HttpSession session) {
        return "redirect:/blog/blogMain";
    }

    // 메인
    @GetMapping("/blogMain")
    public void blogMain(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, String sigungu, Model model) {

        if(sigungu == null){
            model.addAttribute("init", 0);

        } else{
            if(sigungu.contains("/")){
                String[] sigunguSplit = sigungu.split("/");
                sigungu = sigunguSplit[1];
            }
            model.addAttribute("init", 1);
            PageResultsDTO<MemberHelperInfoDTO, Object[]> result = helperInfoService.getHelperInfoByLoc(sigungu, pageRequestDTO);
            model.addAttribute("result", result);
            model.addAttribute("sigungu", sigungu);
            model.addAttribute("count", helperInfoService.countHelpersBySigungu(sigungu));
        }

    }

    //====================================== 메인 관련 끝 ================================//


    //================================ 포스트 관련 시작======================================//
    // 글 목록(아이디별)
    @GetMapping("/blogList")
    public void list(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model, String writer, HttpSession session) {
        if(writer.equals("myBlog")){
            MemberDto memberDto = (MemberDto)session.getAttribute("user");
            String sessionId = memberDto.getId();
            model.addAttribute("result", blogService.getList(sessionId, pageRequestDTO));
            model.addAttribute("host", sessionId);
            model.addAttribute("nick", memberService.userIdCheck(sessionId).getNickname());
        }else {
            model.addAttribute("result", blogService.getList(writer, pageRequestDTO));
            model.addAttribute("host", writer);
            model.addAttribute("nick", memberService.userIdCheck(writer).getNickname());
        }
    }

    // 글 상세보기
    @GetMapping("/detail")
    public void detail(long postNo, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, TempPageRequestDTO tempPageDTO, Model model, HttpSession session){
        BlogDTO blogDTO = blogService.getDetail(postNo);
        PageResultsDTO<BlogCommentDTO, BlogComment> blogCommentDTO = blogCommentService.getComments(postNo, requestDTO);
        // log.info(blogCommentDTO.getDtoList().get(0).getCommentNo());
        List<String> likerList= blogLikeService.getLiker(postNo);
        int likes = blogLikeService.getLikes(postNo);

        model.addAttribute("likerList", likerList);
        model.addAttribute("likes", likes);
        model.addAttribute("detail", blogDTO);
        model.addAttribute("comments", blogCommentDTO);
        model.addAttribute("listPageInfo", tempPageDTO);

        log.info("안녕");
    }

    // 글 쓰기
    @GetMapping("/basicForm")
    public void index(Model model, HttpSession session) {

        MemberDto memberDto = (MemberDto)session.getAttribute("user");
        String sessionId = memberDto.getId();

        List<AuctionBiddingDTO> doneList = auctionService.getAllWithState4ForHelper(sessionId);

        if(!doneList.isEmpty()){
            AuctionBiddingDTO doneJob = doneList.get(0);
            log.info(doneJob.getHelper());
        }

        BlogDTO dto = BlogDTO.builder()
                .title("tempTitle")
                .content("tempContent")
                .writer("tempWriter")
                .build();

        blogService.registerPost(dto);
        Long tempPostNo = blogService.findMaxID();
        model.addAttribute("postNo", tempPostNo);
        model.addAttribute("doneList", doneList);
        //model.addAttribute("index", 0);
    }

    // 글 쓰기 완료 후, 리다이렉트 기능
    @PostMapping(value = "/post")
    public String temp(BlogDTO dto, Model model) {
        blogService.registerPost(dto);
        String result = "redirect:/blog/blogList?writer="+dto.getWriter();
        return result;
    }

    //=================================== 포스트 관련 끝 ===================================//


    //=================================== 파일 업로드 관련 시작 =============================//
    // 파일 업로드
    @ResponseBody
    @PostMapping(value = "/fileUpload" ,produces = "application/json; charset=utf8")
    public String upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("postNo") String postNo) throws Exception {

        JsonObject jsonObject = new JsonObject();
        String fileRoot = "C:\\summernote_image\\";  // 파일이 저장될 절대 경로(상대 경로로 가능, 편의상 임시로 해놓은 것)
        Long no = Long.parseLong(postNo);
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명

        BlogFilesDTO dto = BlogFilesDTO.builder()
                .fileName(savedFileName)
                .fileOrigin(originalFileName)
                .postNo(no)
                .build();

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();    // request받은 multipart를 저장할 수 있게끔 InputStream 생성
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장

            blogFilesService.saveFileInDB(dto);

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
                blogFilesService.deleteFileInDB(filePathNameSplit[3]);
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

    //================================== 파일 업로드 관련 끝 ====================================//



    //================================== 글 수정/삭제 관련 시작 ====================================//

    // 글 수정(이동)
    @GetMapping(value = "/modify")
    public void toModify(long postNo, TempPageRequestDTO tempPageDTO, Model model) {
        BlogDTO blogDTO = blogService.getDetail(postNo);
        model.addAttribute("detail", blogDTO);
        model.addAttribute("listPageInfo", tempPageDTO);
    }

    // 글 수정(기능)
    @PostMapping(value = "/modify")
    public String modifyPost(BlogDTO dto, HttpSession session, Model model) {

        blogService.registerPost(dto);

        MemberDto memberDto = (MemberDto)session.getAttribute("user");
        String sessionId = memberDto.getId();

        List<AuctionBiddingDTO> doneList = auctionService.getAllWithState4ForHelper(sessionId);

        String result = "redirect:/blog/blogList?writer="+dto.getWriter();
        model.addAttribute("doneList", doneList);
        return result;
    }

    // 글 삭제
    @GetMapping(value = "/delete")
    public String deletePost(Long postNo, String writer, TempPageRequestDTO tempPageDTO){

        blogService.deletePost(postNo);

        String url = "redirect:/blog/blogList?writer=" + writer + "&page=" + tempPageDTO.getTempPage();

        return url;
    }


    //================================== 글 수정/삭제 관련 끝 ====================================//


    //================================== 댓글 관련 시작 ====================================//

    // 댓글 작성
    @RequestMapping(method = RequestMethod.POST, value = "/registerC", produces = "application/json; charset=utf8")
    public @ResponseBody ResponseEntity<Long> registerComment(@RequestBody BlogCommentDTO blogCommentDTO) {

        System.out.println("aaa");
        blogCommentService.registerComment(blogCommentDTO);
        return new ResponseEntity<>(1L, HttpStatus.OK);
    }

    // 댓글 삭제
    @ResponseBody
    @DeleteMapping(value = "/deleteComment", produces = "application/json; charset=utf8")
    public ResponseEntity<Long> deleteComment(@RequestBody Long commentNo) {
        log.info(commentNo);
        blogCommentService.deleteComment(commentNo);
        return new ResponseEntity<>(1L, HttpStatus.OK);
    }

    // 댓글 수정
    @ResponseBody
    @PostMapping(value = "/modifyComment", produces = "application/json; charset=utf8")
    public ResponseEntity<Long> modifyComment(@RequestBody BlogCommentDTO blogCommentDTO) {
        log.info(blogCommentDTO.getCommentNo());
        blogCommentService.modifyComment(blogCommentDTO);
        return new ResponseEntity<>(1L, HttpStatus.OK);
    }

    //================================== 글 수정/삭제 관련 끝 ====================================//


    //================================== 좋아요 관련 시작 ====================================//
    @ResponseBody
    @PostMapping(value = "pushLike",  produces = "application/json; charset=utf8")
    public ResponseEntity<Long> pushLike(@RequestBody BlogLikeDTO blogLikeDTO){
        blogLikeService.pushLike(blogLikeDTO);
        return new ResponseEntity<>(1L, HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "pushLike",  produces = "application/json; charset=utf8")
    public ResponseEntity<Long> cancelLike(@RequestBody BlogLikeDTO blogLikeDTO){
        blogLikeService.cancelLike(blogLikeDTO.getPostNo(), blogLikeDTO.getLiker());
        return new ResponseEntity<>(1L, HttpStatus.OK);
    }

    //================================== 좋아요 관련 끝 ====================================//
}
