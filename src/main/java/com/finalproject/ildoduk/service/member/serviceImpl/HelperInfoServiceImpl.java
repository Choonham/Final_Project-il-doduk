package com.finalproject.ildoduk.service.member.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.blog.BlogDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
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

        Optional<HelperInfo> helperInfo =  repository.findByMemberId(memberId);

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

    //헬퍼 가입을 위한 state 체크
    @Override
    public HelperInfoDTO checkState() {
        log.info("헬퍼 가입 신청 1");
        Optional<HelperInfo> result = repository.checkState();
        log.info("헬퍼 가입 신청 2" + result.get());
        return result.isPresent() ? EntityToDTO(result.get()) : null;
    }
    //승인
    @Override
    public void accept(HelperInfoDTO helperInfoDTO) {
       HelperInfo entity = dtoToEntity(helperInfoDTO);

       repository.save(entity);
    }
    //반려
    @Override
    public void deny(HelperInfoDTO helperInfoDTO) {
        HelperInfo entity = dtoToEntity(helperInfoDTO);

        repository.save(entity);
    }
}
