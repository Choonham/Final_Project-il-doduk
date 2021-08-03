package com.finalproject.ildoduk.service.member.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.blog.BlogDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.repository.member.HelperInfoRepository;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Log4j2
public class HelperInfoServiceImpl implements HelperInfoService {

    HelperInfoRepository repository;

    @Override
    public PageResultsDTO<HelperInfoDTO, HelperInfo> getHelperInfoByLoc(String sigungu, PageRequestDTO requestDTO) {

        // 정렬 방식 설정
        Pageable pageable = requestDTO.getPageable(Sort.by("kindness").descending());
        Page<HelperInfo> result = repository.selectDistinctBySigungu(pageable, sigungu);

        Function<HelperInfo, HelperInfoDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultsDTO<>(result, fn);
    }
}
