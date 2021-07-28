package com.finalproject.ildoduk.service.blog.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.blog.BlogCommentDTO;
import com.finalproject.ildoduk.entity.blog.BlogComment;
import com.finalproject.ildoduk.entity.blog.QBlogComment;
import com.finalproject.ildoduk.repository.blog.BlogCommentRepository;
import com.finalproject.ildoduk.service.blog.service.BlogCommentService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class BlogCommentServiceImpl implements BlogCommentService {
    private final BlogCommentRepository repository;

    @Override
    public PageResultsDTO<BlogCommentDTO, BlogComment> getComments(Long postNo, PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("commentNo").descending());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QBlogComment qBlogComment = QBlogComment.blogComment;

        booleanBuilder.and(qBlogComment.blog.postNo.eq(postNo));

        Page<BlogComment> result = repository.findAll(booleanBuilder, pageable);

        Function<BlogComment, BlogCommentDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultsDTO<>(result, fn);
    }

    @Override
    public void registerComment(BlogCommentDTO dto) {
        BlogComment entity = dtoToEntity(dto);
        repository.save(entity);
    }

    @Override
    public void deleteComment(Long commentNo) {
        repository.deleteById(commentNo);
    }

    @Override
    @Transactional
    public void deleteAllCommentOnThePost(Long postNo) {
        repository.deleteAllByBlog_PostNo(postNo);
    }

    @Override
    public String modifyComment(BlogCommentDTO dto) {
        BlogComment entity = dtoToEntity(dto);
        repository.save(entity);
        return dto.getContent();
    }
}
