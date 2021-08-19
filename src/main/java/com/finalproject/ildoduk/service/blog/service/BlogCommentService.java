package com.finalproject.ildoduk.service.blog.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.blog.BlogCommentDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.blog.BlogComment;
import com.finalproject.ildoduk.entity.member.Member;

public interface BlogCommentService {

    // 댓글 불러오기
    PageResultsDTO<BlogCommentDTO, BlogComment> getComments(Long postNo, PageRequestDTO requestDTO);

    // 댓글 등록
    void registerComment(BlogCommentDTO dto);

    // 댓글 삭제
    void deleteComment(Long commentNo);

    // 해당 포스트의 모든 댓글 삭제
    void deleteAllCommentOnThePost(Long postNo);

    // 댓글 수정
    String modifyComment(BlogCommentDTO dto);

    default BlogComment dtoToEntity(BlogCommentDTO dto){

        Blog blog = Blog.builder().postNo(dto.getPostNo()).build();

        Member writer = Member.builder()
                .id(dto.getWriter())
                .build();

        BlogComment entity = BlogComment.builder()
                .commentNo(dto.getCommentNo())
                .blog(blog)
                .content(dto.getContent())
                .writer(writer)
                .build();
        return entity;
    }

    default BlogCommentDTO entityToDTO(BlogComment entity){
        BlogCommentDTO dto = BlogCommentDTO.builder()
                .commentNo(entity.getCommentNo())
                .postNo(entity.getBlog().getPostNo())
                .content(entity.getContent())
                .writer(entity.getWriter().getId())
                .writerInfo(entity.getWriter())
                .regDate(entity.getRegDate())
                .build();
        return dto;
    }


}
