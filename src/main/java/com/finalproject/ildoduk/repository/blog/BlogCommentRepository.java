package com.finalproject.ildoduk.repository.blog;

import com.finalproject.ildoduk.entity.blog.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long>, QuerydslPredicateExecutor<BlogComment> {
    void deleteAllByBlog_PostNo(Long postNo);
}
