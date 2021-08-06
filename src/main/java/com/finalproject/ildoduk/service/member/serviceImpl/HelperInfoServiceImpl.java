package com.finalproject.ildoduk.service.member.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.blog.BlogDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.member.QHelperInfo;
import com.finalproject.ildoduk.repository.member.HelperInfoRepository;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class HelperInfoServiceImpl implements HelperInfoService {

    private final HelperInfoRepository repository;

    @Override
    public HelperInfoDTO helperFindById(String memberId) {

        Optional<HelperInfo> helperInfo =  repository.findById(memberId);

        return helperInfo.isPresent() ? EntityToDTO(helperInfo.get()) : null;
    }

    @Override
    public PageResultsDTO<MemberHelperInfoDTO, Object[]> getHelperInfoByLoc(String sigungu, PageRequestDTO requestDTO) {


        // 정렬 방식 설정
        log.info(sigungu);
        Pageable pageable = requestDTO.getPageable(Sort.by("kindness").descending());
        Page<Object[]> result = repository.selectDistinctBySigungu(pageable, sigungu);
        Function<Object[], MemberHelperInfoDTO> fn = (entity -> entityToDTO((HelperInfo) entity[0], (Member) entity[1]));

        return new PageResultsDTO<>(result, fn);
    }

    @Override
    public int countHelpersBySigungu(String sigungu) {
        return repository.countDistinctBySigungu(sigungu);
    }
}
