package com.finalproject.ildoduk.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultsDTO<DTO, EN> {

    // DTO 리스트
    private List<DTO> dtoList;

    // 총 페이지
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size;

    // 시작 페이지 번호
    private int start;

    // 끝 페이지 번호
    private int end;

    private boolean prev, next;

    // 페이지 번호 목록
    private List<Integer> pageList;

    //총 엘리먼트 갯수
    private Long totalElements;

    public PageResultsDTO(Page<EN> result, Function<EN, DTO> fn){ // 2.
        dtoList = result.stream().map(fn).collect(Collectors.toList()); // DTO 리스트
        totalPage = result.getTotalPages(); // 전체 페이지
        totalElements=result.getTotalElements(); //전체 Elements
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1; // 0부터 시작하기 때문에 1추가
        this.size = pageable.getPageSize();

        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd: totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }

}
