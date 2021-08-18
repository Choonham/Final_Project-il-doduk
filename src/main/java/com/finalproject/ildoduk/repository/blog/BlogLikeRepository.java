package com.finalproject.ildoduk.repository.blog;

import com.finalproject.ildoduk.entity.blog.BlogLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BlogLikeRepository extends JpaRepository<BlogLike, Long>, QuerydslPredicateExecutor<BlogLike> {
    void deleteByBlog_PostNoAndLiker_Id(long postNO, String liker);
    List<BlogLike> findAllByBlog_PostNo(long postNO);
    int countAllByBlog_PostNo(long postNo);


}
