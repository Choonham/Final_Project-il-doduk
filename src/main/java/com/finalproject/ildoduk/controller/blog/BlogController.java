package com.finalproject.ildoduk.controller.blog;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.auction.AuctionBiddingDTO;
import com.finalproject.ildoduk.dto.blog.*;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.blog.BlogComment;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.service.auction.service.AuctionService;
import com.finalproject.ildoduk.service.blog.service.BlogCommentService;
import com.finalproject.ildoduk.service.blog.service.BlogFilesService;
import com.finalproject.ildoduk.service.blog.service.BlogLikeService;
import com.finalproject.ildoduk.service.blog.service.BlogService;
import com.finalproject.ildoduk.service.covid_19.CovidCheckService;
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

import java.lang.reflect.Array;
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

    //=================================== ?????? ?????? ??????===================================//

    // "/" ????????? ?????? ??????
    @GetMapping("/")
    public String main(HttpSession session) {
        return "redirect:/blog/blogMain";
    }

    // ??????
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

    //====================================== ?????? ?????? ??? ================================//


    //================================ ????????? ?????? ??????======================================//
    // ??? ??????(????????????)
    @RequestMapping(value = "/blogList", method = RequestMethod.GET)
    public void list(String writer, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model, HttpSession session) {
        blogService.deleteTempPost("tempContent");

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

    // ??? ??????
    @ResponseBody
    @RequestMapping(value = "/blogSearch", method = RequestMethod.GET)
    public PageResultsDTO<BlogDTO, Blog> search(@RequestParam("writer") String writer, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO) {
        return blogService.getList(writer, pageRequestDTO);
    }


    // ??? ????????????
    @GetMapping("/detail")
    public void detail(long postNo, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, TempPageRequestDTO tempPageDTO, Model model, HttpSession session){
        BlogDTO blogDTO = blogService.getDetail(postNo);
        PageResultsDTO<BlogCommentDTO, BlogComment> blogCommentDTO = blogCommentService.getComments(postNo, requestDTO);
        // log.info(blogCommentDTO.getDtoList().get(0).getCommentNo());
        List<String> likerList= blogLikeService.getLiker(postNo);
        int likes = blogLikeService.getLikes(postNo);
        String writer = blogDTO.getWriter().getId();

        //MemberDto writerInfoMember = memberService.userIdCheck(writer);
        HelperInfoDTO writerInfoHelper = helperInfoService.helperFindById(writer);
        //log.info(writerInfoMember.getPhoto());

        model.addAttribute("likerList", likerList);
        model.addAttribute("likes", likes);
        model.addAttribute("detail", blogDTO);
        model.addAttribute("comments", blogCommentDTO);
        model.addAttribute("listPageInfo", tempPageDTO);

        //model.addAttribute("writerInfoMember", writerInfoMember);
        model.addAttribute("writerInfoHelper", writerInfoHelper);

    }

    // ??? ??????
    @GetMapping("/basicForm")
    public void index(Model model, HttpSession session) {

        MemberDto memberDto = (MemberDto)session.getAttribute("user");
        String sessionId = memberDto.getId();

        List<AuctionBiddingDTO> doneList = auctionService.getAllWithState4ForHelper(sessionId);
        Member member = Member.builder()
                .id(sessionId)
                .build();

        if(!doneList.isEmpty()){
            AuctionBiddingDTO doneJob = doneList.get(0);
            log.info(doneJob.getHelper());
        }

        BlogDTO dto = BlogDTO.builder()
                .title("tempTitle")
                .content("tempContent")
                .writer(member)
                .build();

        blogService.registerPost(dto);
        Long tempPostNo = blogService.findMaxID();
        model.addAttribute("postNo", tempPostNo);
        model.addAttribute("doneList", doneList);
        //model.addAttribute("index", 0);
    }

    // ??? ?????? ?????? ???, ??????????????? ??????
    @PostMapping(value = "/post")
    public String temp(BlogDTO dto, Model model) {
        log.info(dto.getContent());
        blogService.registerPost(dto);
        String result = "redirect:/blog/blogList?writer="+dto.getWriter().getId();
        return result;
    }

    //=================================== ????????? ?????? ??? ===================================//


    //=================================== ?????? ????????? ?????? ?????? =============================//
    // ?????? ?????????
    @ResponseBody
    @PostMapping(value = "/fileUpload" ,produces = "application/json; charset=utf8")
    public String upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("postNo") String postNo) throws Exception {

        JsonObject jsonObject = new JsonObject();
        String fileRoot = "C:\\summernote_image\\";  // ????????? ????????? ?????? ??????(?????? ????????? ??????, ????????? ????????? ????????? ???)
        Long no = Long.parseLong(postNo);
        String originalFileName = multipartFile.getOriginalFilename();	//???????????? ?????????
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//?????? ?????????
        String savedFileName = UUID.randomUUID() + extension;	//????????? ?????? ???

        BlogFilesDTO dto = BlogFilesDTO.builder()
                .fileName(savedFileName)
                .fileOrigin(originalFileName)
                .postNo(no)
                .build();

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();    // request?????? multipart??? ????????? ??? ????????? InputStream ??????
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//?????? ??????

            blogFilesService.saveFileInDB(dto);

            jsonObject.addProperty("url", "/resources/fileupload/"+savedFileName); // contextroot + resources + ????????? ?????? ?????????
            jsonObject.addProperty("responseCode", "success");
            jsonObject.addProperty("savedFileName", savedFileName);
            jsonObject.addProperty("originalFileName", originalFileName);

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//????????? ?????? ??????
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    // ????????? ???????????? ?????? ??????
    @ResponseBody
    @PostMapping(value = "/fileDelete", produces = "application/json; charset=utf8")
    public String delete(@RequestParam("filePathName") String filePathName){ // img ?????? ?????? src ????????? String ?????? ?????? ??????

        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\summernote_image\\";  // ????????? ????????? ?????? ??????

        log.info(filePathName); //  => ex: /resources/fileupload/67b7876f-976a-4c66-a835-99dd4f09eb4e.jpg
        String[] filePathNameSplit = filePathName.split("/"); // "/" ??? ???????????? split

        String fileServerPath = fileRoot + filePathNameSplit[3]; // ????????? ?????? ??????

        File fileInServer = new File(fileServerPath); // ?????? ?????? ??????

        try{
            if(fileInServer.exists()){ // ????????? ?????????,
                fileInServer.delete(); // ?????? ??????
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

    //================================== ?????? ????????? ?????? ??? ====================================//



    //================================== ??? ??????/?????? ?????? ?????? ====================================//

    // ??? ??????(??????)
    @GetMapping(value = "/modify")
    public void toModify(long postNo, TempPageRequestDTO tempPageDTO, Model model) {
        BlogDTO blogDTO = blogService.getDetail(postNo);
        model.addAttribute("detail", blogDTO);
        model.addAttribute("listPageInfo", tempPageDTO);
    }

    // ??? ??????(??????)
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

    // ??? ??????
    @GetMapping(value = "/delete")
    public String deletePost(Long postNo, String writer, TempPageRequestDTO tempPageDTO){

        blogService.deletePost(postNo);

        String url = "redirect:/blog/blogList?writer=" + writer + "&page=" + tempPageDTO.getTempPage();

        return url;
    }


    //================================== ??? ??????/?????? ?????? ??? ====================================//


    //================================== ?????? ?????? ?????? ====================================//

    // ?????? ??????
    @RequestMapping(method = RequestMethod.POST, value = "/registerC", produces = "application/json; charset=utf8")
    public @ResponseBody ResponseEntity<Long> registerComment(@RequestBody BlogCommentDTO blogCommentDTO) {
        blogCommentService.registerComment(blogCommentDTO);
        return new ResponseEntity<>(1L, HttpStatus.OK);
    }

    // ?????? ??????
    @ResponseBody
    @DeleteMapping(value = "/deleteComment", produces = "application/json; charset=utf8")
    public ResponseEntity<Long> deleteComment(@RequestBody Long commentNo) {
        log.info(commentNo);
        blogCommentService.deleteComment(commentNo);
        return new ResponseEntity<>(1L, HttpStatus.OK);
    }

    // ?????? ??????
    @ResponseBody
    @PostMapping(value = "/modifyComment", produces = "application/json; charset=utf8")
    public ResponseEntity<Long> modifyComment(@RequestBody BlogCommentDTO blogCommentDTO) {
        log.info(blogCommentDTO.getCommentNo());
        blogCommentService.modifyComment(blogCommentDTO);
        return new ResponseEntity<>(1L, HttpStatus.OK);
    }

    //================================== ??? ??????/?????? ?????? ??? ====================================//


    //================================== ????????? ?????? ?????? ====================================//
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

    //================================== ????????? ?????? ??? ====================================//
}
