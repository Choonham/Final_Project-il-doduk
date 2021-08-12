package com.finalproject.ildoduk.service.member.serviceImpl;

import com.finalproject.ildoduk.dto.PageRequestDTO;
import com.finalproject.ildoduk.dto.PageResultsDTO;
import com.finalproject.ildoduk.dto.blog.BlogDTO;
import com.finalproject.ildoduk.dto.member.HelperInfoDTO;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.entity.blog.Blog;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.entity.member.QHelperInfo;
import com.finalproject.ildoduk.entity.pay.Payment;
import com.finalproject.ildoduk.repository.member.HelperInfoRepository;
import com.finalproject.ildoduk.repository.member.MemberRepository;
import com.finalproject.ildoduk.service.member.service.HelperInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private HelperInfoRepository repository;
    @Autowired
    private MemberRepository repo;
    @Override
    public int helperRegister(HelperInfoDTO helperInfoDTO) {

            helperInfoDTO.setAgreeHelper(1); //헬퍼 신청하였으니 헬퍼신청대기중 상태로 변경
        repository.save(dtoToEntity(helperInfoDTO)); // 헬퍼 신청 정보 일단 DB에 저장
        int cnt = 1;

        return cnt;
    }

    @Override
    public int helperRegisterIdCheck(Member memberId) {

        int i = repository.countHelperInfoByMemberId(memberId);

        log.info("helperRegisterIdCheck :::: " + i);

        return i;
    }

    @Override
    public HelperInfoDTO helperFindById(String memberId) {

        //String member -> member member 로 로드하고 밑에 findByMemberId로 넣어야함

        // memo by 노영준 : repo에서 Optional<HelperInfo> findByMemberId_Id(String memberId); 로 사용하면 쿼리 두번 안불러도 되지 않을까요...?

        /*Optional<Member> member= repo.findById(memberId);

        Optional<HelperInfo> helperInfo =  repository.findByMemberId(member.get());
*/
        Optional<HelperInfo> helperInfo = repository.findByMemberId_Id(memberId);

        return helperInfo.isPresent() ? EntityToDTO(helperInfo.get()) : null;
    }

    //=======================auction 시작==============================//

    //헬퍼 아이디체크 후 헬퍼인포 반환(HelperInfo)
    @Override
    public MemberHelperInfoDTO helperFindById2(String memberId) {

        Optional<HelperInfo> helperInfo =  repository.findByMemberId_Id(memberId);

        return helperInfo.isPresent() ? entityToDTO(helperInfo.get()) : null;
    }
    //=======================auction 끝================================//

    // =========================Blog======================= //

    // 활동 지역으로 헬퍼 찾기
    @Override
    public PageResultsDTO<MemberHelperInfoDTO, Object[]> getHelperInfoByLoc(String sigungu, PageRequestDTO requestDTO) {


        // 정렬 방식 설정
        log.info(sigungu);
        Pageable pageable = requestDTO.getPageable(Sort.by("kindness").descending());
        Page<Object[]> result = repository.selectDistinctBySigungu(pageable, sigungu);
        Function<Object[], MemberHelperInfoDTO> fn = (entity -> entityToDTO((HelperInfo) entity[0], (Member) entity[1]));

        return new PageResultsDTO<>(result, fn);
    }

    // 활동 지역별 헬퍼 수
    @Override
    public int countHelpersBySigungu(String sigungu) {
        return repository.countDistinctBySigungu(sigungu);
    }


    // =========================Blog======================= //


//--------- 관리자 : 헬퍼 리스트 ---------------
    //헬퍼 가입을 위한 state 체크

    @Override
    public PageResultsDTO<HelperInfoDTO, HelperInfo> agreeHelperOne(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("helperNo").descending());
        Page<HelperInfo> result = repository.findAllOne(pageable);

        Function<HelperInfo, HelperInfoDTO> fn = (entity -> EntityToDTO(entity));

        return new PageResultsDTO<>(result,fn);
    }

    @Override
    public PageResultsDTO<HelperInfoDTO, HelperInfo> agreeHelperTwo(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("helperNo").descending());
        Page<HelperInfo> result = repository.findAllTwo(pageable);

        Function<HelperInfo, HelperInfoDTO> fn = (entity -> EntityToDTO(entity));

        return new PageResultsDTO<>(result,fn);
    }

    @Override
    public PageResultsDTO<HelperInfoDTO, HelperInfo> agreeHelperThree(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("helperNo").descending());
        Page<HelperInfo> result = repository.findAllThree(pageable);

        Function<HelperInfo, HelperInfoDTO> fn = (entity -> EntityToDTO(entity));

        return new PageResultsDTO<>(result,fn);
    }

    //승인
    @Override
    public void accept(HelperInfoDTO helperInfoDTO) {
       Optional<HelperInfo> result = repository.findById(helperInfoDTO.getHelperNo());

       HelperInfo entity = result.get();
       log.info("저장된 헬퍼 정보 ~~~~~ "+entity);

       entity.changeAgreeHelper(2);
       repository.save(entity);
    }

    //반려
    @Override
    public void deny(HelperInfoDTO helperInfoDTO) {
        Optional<HelperInfo> result = repository.findById(helperInfoDTO.getHelperNo());
        HelperInfo entity = result.get();

        entity.changeAgreeHelper(3);
        repository.save(entity);
    }

    @Override
    public HelperInfoDTO helperInfo(HelperInfoDTO helperInfoDTO) {
        Optional<HelperInfo> result = repository.findById(helperInfoDTO.getHelperNo());
        HelperInfo entity = result.get();

        return EntityToDTO(entity);
    }

    //헬퍼 정보 수정
    @Override
    public void helperModify(HelperInfoDTO helperInfoDTO) {
        Optional<HelperInfo> helperInfo = repository.findByMemberId_Id(helperInfoDTO.getMemberId());

        if(helperInfo.isPresent()){
            HelperInfo entity = helperInfo.get();

            entity.changeGoodAtFirst(helperInfoDTO.getGoodAtFirst());
            entity.changeGoodAtSecond(helperInfoDTO.getGoodAtSecond());
            entity.changeGoodAtThird(helperInfoDTO.getGoodAtThird());
            entity.changeAppeal(helperInfoDTO.getAppeal());
            entity.changeImg(helperInfoDTO.getImg());
            repository.save(entity);
        }
    }


}
