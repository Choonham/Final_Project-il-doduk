package com.finalproject.ildoduk.service.blog.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.blog.BlogDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.blog.QBlog;
import com.finalproject.ildoduk.entity.member.QMember;
import com.finalproject.ildoduk.repository.blog.BlogRepository;
import com.finalproject.ildoduk.service.blog.service.BlogService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class BlogServiceImpl implements BlogService {

    private final BlogRepository repository;

    @Override
    public Long findMaxID() {
        return repository.max();
    }

    @Override
    public BlogDTO getDetail(long postNo) {
        Optional<Blog> result = repository.findById(postNo);
        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    @Override
    public PageResultsDTO<BlogDTO, Blog> getList(String writer, PageRequestDTO requestDTO) {

        // 정렬 방식 설정
        Pageable pageable = requestDTO.getPageable(Sort.by("postNo").descending());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QBlog qBlog = QBlog.blog;


        booleanBuilder.and(qBlog.writer.id.contains(writer));

        BooleanBuilder searchBuilder = getSearch(requestDTO);

        booleanBuilder.and(searchBuilder);

        Page<Blog> result = repository.findAll(booleanBuilder, pageable); // 1. Page<Entity> 객체로 반환하는 findAll

        Function<Blog, BlogDTO> fn = (entity -> entityToDTO(entity)); // 2. Function<T, R> => 람다식을 지원하는 함수형 인터페이스
        // => Function<String, String> upperCase = v -> v.toUpperCase();
        // 위와 같이 input Type 과 output Type 을 = 뒤에 오는 인자를 추정할 수 있게끔 한다.

        return new PageResultsDTO<>(result, fn);
    }

    @Override
    public Long registerPost(BlogDTO dto) {
        Blog entity = dtoToEntity(dto);
        log.info(entity.getContent());
        repository.save(entity);

        return dto.getPostNo();
    }

    @Override
    public int deletePost(long postNo) {
        repository.deleteById(postNo);
        return 1;
    }

    @Override
    @Transactional
    public void deleteTempPost(String content) {
        repository.deleteAllByContent(content);
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO){

        // 검색 타입
        String type = requestDTO.getType();

        // BooleanBuilder 객체 생성
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QBlog qBlog = QBlog.blog;

        // 검색어
        String keyword = requestDTO.getKeyword();

        BooleanExpression expression = qBlog.postNo.gt(0L);
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0){
            return booleanBuilder;
        }

        // 검색 조건이 존재
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){
            conditionBuilder.or(qBlog.title.contains(keyword));
        }

        if(type.contains("c")){
            conditionBuilder.or(qBlog.content.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
