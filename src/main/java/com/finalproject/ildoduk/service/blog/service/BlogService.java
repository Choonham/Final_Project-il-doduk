package com.finalproject.ildoduk.service.blog.service;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.blog.BlogDTO;
import com.finalproject.ildoduk.entity.blog.Blog;

public interface BlogService {

    // 최상단 포스트 번호 가져오기
    Long findMaxID();

    // 포스트 상세보기
    BlogDTO getDetail(long postNo);

    // 포스트 목록 가져오기(미리보기 이미지 & 서비스 제목 & 날짜)
    PageResultsDTO<BlogDTO, Blog> getList(String writer, PageRequestDTO requestDTO);

    // 포스트 등록
    Long registerPost(BlogDTO dto);

    // 포스트 삭제
    int deletePost(long postNo);


    default Blog dtoToEntity(BlogDTO dto) {
        Blog entity = Blog.builder()
                .postNo(dto.getPostNo())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .thumbnail(dto.getThumbnail())
                .build();
        return entity;
    }

    default BlogDTO entityToDTO(Blog entity) {
        BlogDTO dto = BlogDTO.builder()
                .postNo(entity.getPostNo())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .thumbnail(entity.getThumbnail())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

}
