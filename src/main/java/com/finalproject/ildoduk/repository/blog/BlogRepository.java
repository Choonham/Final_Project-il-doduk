package com.finalproject.ildoduk.repository.blog;

import com.finalproject.ildoduk.entity.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BlogRepository extends JpaRepository<Blog, Long>, QuerydslPredicateExecutor<Blog> {

    @Query(value = "SELECT max(postNo) FROM Blog")
    public Long max();
}
