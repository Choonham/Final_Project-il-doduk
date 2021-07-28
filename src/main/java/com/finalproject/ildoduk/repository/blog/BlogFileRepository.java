package com.finalproject.ildoduk.repository.blog;

import com.finalproject.ildoduk.entity.blog.BlogFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BlogFileRepository extends JpaRepository<BlogFiles, Long>, QuerydslPredicateExecutor<BlogFiles> {
    void deleteByFileName(String fileName);
    void deleteAllByBlog_PostNo(Long postNo);
    List<BlogFiles> findAllByBlog_PostNo(Long postNo);
}
