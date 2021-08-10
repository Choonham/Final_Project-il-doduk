package com.finalproject.ildoduk.service.blog.service;

import com.finalproject.ildoduk.dto.blog.BlogLikeDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.blog.BlogLike;
import com.finalproject.ildoduk.entity.member.Member;

import java.util.List;

public interface BlogLikeService {

    // push 좋아요
    void pushLike(BlogLikeDTO dto);

    // cancel 좋아요
    void cancelLike(long postNo, String liker);

    // 좋아요 누른 사람 리스트
    List<String> getLiker(long postNo);

    // 좋아요 개수
    int getLikes(long postNo);

    default BlogLike dtoToEntity(BlogLikeDTO dto){

        Blog blog = Blog.builder().postNo(dto.getPostNo()).build();

        Member writer = Member.builder()
                .id(dto.getLiker())
                .build();

        BlogLike entity = BlogLike.builder()
                .likeNo(dto.getLikeNo())
                .blog(blog)
                .liker(writer)
                .build();
        return entity;
    }

    default BlogLikeDTO entityToDTO(BlogLike entity){
        BlogLikeDTO dto = BlogLikeDTO.builder()
                .likeNo(entity.getLikeNo())
                .postNo(entity.getBlog().getPostNo())
                .liker(entity.getLiker().getId())
                .build();
        return dto;
    }

}
