package com.finalproject.ildoduk.service.blog.service;

import com.finalproject.ildoduk.dto.blog.BlogFilesDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.blog.BlogFiles;
import javafx.scene.input.DataFormat;

import java.util.Date;

public interface BlogFilesService {

    // 파일 저장
    void saveFileInDB(BlogFilesDTO dto);

    // 파일 삭제
    void deleteFileInDB(String fileName);

    // 해당 포스트의 모든 파일 삭제
    void deleteAllFileOnThePost(Long postNo);

    default BlogFilesDTO entityToDTO(BlogFiles entity){

        BlogFilesDTO dto = BlogFilesDTO.builder()
                .fileNo(entity.getFileNo())
                .fileName(entity.getFileName())
                .fileOrigin(entity.getFileOrigin())
                .postNo(entity.getBlog().getPostNo())
                .build();
        return dto;
    }

    default BlogFiles dtoToEntity(BlogFilesDTO dto){

        Blog blog = Blog.builder().postNo(dto.getPostNo()).build();

        BlogFiles entity = BlogFiles.builder()
                .fileNo(dto.getFileNo())
                .fileName(dto.getFileName())
                .blog(blog)
                .fileOrigin(dto.getFileOrigin())
                .build();
        return entity;
    }

}
