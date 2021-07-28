package com.finalproject.ildoduk.service.blog.serviceImpl;

import com.finalproject.ildoduk.dto.blog.BlogLikeDTO;
import com.finalproject.ildoduk.entity.blog.BlogLike;
import com.finalproject.ildoduk.repository.blog.BlogLikeRepository;
import com.finalproject.ildoduk.service.blog.service.BlogLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class BlogLikeServiceImpl implements BlogLikeService {
    private final BlogLikeRepository repository;

    // 좋아요 누르기
    @Override
    public void pushLike(BlogLikeDTO dto) {
        BlogLike entity = dtoToEntity(dto);
        repository.save(entity);
    }

    // 좋아요 취소
    @Override
    @Transactional
    public void cancelLike(long postNo, String liker) {
        repository.deleteByBlog_PostNoAndLiker(postNo, liker);
    }

    // 좋아요 누른 사람 리스트
    @Override
    public List<String> getLiker(long postNo) {
        List<BlogLike> entityList = repository.findAllByBlog_PostNo(postNo);
        List<String> resultList = new ArrayList<>();

        for(BlogLike entity : entityList){
            resultList.add(entity.getLiker());
        }

        return resultList;
    }

    // 좋아요 개수
    @Override
    public int getLikes(long postNo) {
        return repository.countAllByBlog_PostNo(postNo);
    }
}
