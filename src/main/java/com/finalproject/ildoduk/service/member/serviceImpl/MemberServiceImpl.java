package com.finalproject.ildoduk.service.member.serviceImpl;

import com.finalproject.ildoduk.dto.member.MemberDto;

import com.finalproject.ildoduk.dto.member.MemberHelperInfoDTO;
import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.entity.member.HelperInfo;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.repository.member.HelperInfoRepository;
import com.finalproject.ildoduk.repository.member.MemberRepository;
import com.finalproject.ildoduk.service.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class MemberServiceImpl implements MemberService {


    private final MemberRepository repo;
    private final HelperInfoRepository helperInfoRepository;


    @Override
    public List<String> getList() {

        List<String> list_st = new ArrayList<>();
        List<Member> list=repo.findAll();
        for (Member m: list
             ) {
              list_st.add(m.getId());

        }
        return list_st;
    }

    //회원가입
    @Override
    public void userRegister(MemberDto dto) {

        repo.save(dtoToEntity(dto));


    }

    //유저 닉네임 db확인
    @Override
    public int nickNameCheck(String nickname) {

       int cnt = repo.countByNickname(nickname);
       log.info("nicknameCheck result ::: " + cnt);

        return cnt;
    }
    //해당 닉네임에 관련된 정보 가져오기
    @Override
    public MemberDto userNickCheck(String nick) {
        Optional<Member> member = repo.findAllByNickname(nick);

        return member.isPresent() ? EntityToDto(member.get()) : null;
    }


    //유저 아이디 DB에서 확인
    public MemberDto userIdCheck(String id) {

        Optional<Member> result = repo.findById(id);

        log.info(" userIdCheck result ::::::" + result);

        return result.isPresent() ? EntityToDto(result.get()) : null;

    }

    //회원 수정 관련
    @Override
    public void userModify(MemberDto dto) {
        Optional<Member> result = repo.findById(dto.getId());

        if(result.isPresent()){

            Member entity = result.get();

            entity.changeNickname(dto.getNickname());
            entity.changePhone(dto.getPhone());
            entity.changeAddress(dto.getAddress());
            entity.changeSido(dto.getSido());
            entity.changeSigungu(dto.getSigungu());
            entity.changePhoto(dto.getPhoto());
            entity.changeIntro(dto.getIntro());
            repo.save(entity);
        }
    }


    //헬퍼 승인 시에 state 값 2로 변경
    @Override
    public void updateState(MemberDto memberDto) {

        Optional<Member> result = repo.findById(memberDto.getId());

        Member entity = result.get();

        memberDto.setState(2);
        entity.changeState(memberDto.getState());

        repo.save(entity);



    }


    //회원 탈퇴
    @Override
    public void userDelete(String id) {
        System.out.println("딜리트ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ1");
        Member member =  repo.findById(id).get();

        System.out.println("딜리트ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ2 :::: "  + member.getState() );
        member.changeState(3);
        System.out.println("딜리트ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ3 :::: " + member.getState());

        repo.save(member);

    }
    @Override
    public MemberDto userToHelperIdCheck(String memberId){
        Optional<Member> result = repo.findById(memberId);

        log.info(" userIdCheck result ::::::" + result);

        return result.isPresent() ?  EntityToDto(result.get()) : null;
    }



    //카카오 로그인 관련 JSON parser -> return dto;
    @Override
    public MemberDto kakaoLogin(String json)  {
        MemberDto ud = new MemberDto();
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        JSONObject jsonObj2;


        MemberDto dto = new MemberDto();

        try{
            jsonObj =(JSONObject)parser.parse(json);

            String email = (String)jsonObj.get("email");    //아이디
            String gender = (String)jsonObj.get("gender");  //성별
            String birth = (String)jsonObj.get("birthday"); // 생일
            String profile = jsonObj.get("profile").toString(); //profile 은 값안에 키, 값이 또 있어서 한번 쪼개고
            jsonObj2 = (JSONObject)parser.parse(profile);//한번더 쪼개기
            String nickname = (String)jsonObj2.get("nickname"); //닉네임 추출
            String img = (String)jsonObj2.get("thumbnail_image_url");//이미지 추출

            dto.setId(email);
            dto.setNickname(nickname);

            if(gender.equals("male")){
            dto.setGender("남");
            }else{
                dto.setGender("여");
            }
            dto.setBirth(birth);
            dto.setPhoto(img);

            System.out.println(" kakaoLogin :: dto get id::::  " + dto.getId());
            System.out.println(" kakaoLogin :: dto get gender::::  " + dto.getGender());
            System.out.println(" kakaoLogin :: dto get nickname:::   " + dto.getNickname());
            System.out.println(" kakaoLogin :: dto get birth:::   " + dto.getBirth());
            System.out.println(" kakaoLogin :: dto get photo:::   " + dto.getPhoto());


        }catch(ParseException e){
            System.out.println("Json parse err" + e);
        }
        return dto;
    }
//---------   유저 포인트 관련  -----------
//유저 포인트 증가
@Override
public void updatePoint(PaymentDTO dto) {
    //전달 받은 데이터 : 결제금액, 해당 아이디
    int point = (int)(Math.ceil(dto.getTotalPoint() * 1.0));

    String userID = dto.getUserId();

    log.info("충전할 금액 " + point);

    Optional<Member> result = repo.findById(userID);

    if(result.isPresent()){

        Member entity = result.get();
        repo.pointUpdate(point,entity.getId());
    }
}

    //경매 등록시에 포인트 차감(보증금 걸어놓는것처럼)
    @Override
    public void minusPoint(MemberDto dto) {

        int point = dto.getPoint();
        String userID = dto.getId();

        Optional<Member> result = repo.findById(userID);

        if(result.isPresent()){

            Member entity = result.get();
            log.info("로직 내부 entity 값 조회한 결과 " + entity);
            log.info("퍼시턴스에 들어온 point " + point);

            int userPoint = entity.getPoint();

            int totalPoint = (int)(Math.ceil((userPoint-point)*1.0));

            repo.pointMinus(totalPoint, entity.getId());
        }
    }

    //경매 미매칭시에 다시 원래 금액 돌려줌
    @Override
    public void refundAuctionPay(MemberDto dto) {
        int point = dto.getPoint();
        String userID = dto.getId();

        Optional<Member> result = repo.findById(userID);

        if(result.isPresent()){

            Member entity = result.get();

            int userPoint = entity.getPoint();
            int totalPoint = (int)(Math.ceil((userPoint-point)*1.0));

            repo.pointUpdate(totalPoint, entity.getId());
        }
    }

    //헬퍼 <-> 유저 전환
    @Override
    public void changeUser(MemberHelperInfoDTO memberHelperInfoDTO) {
        log.info("변경 내부 넘어온 데이터~~~~~~~~~"+memberHelperInfoDTO);

        Optional<Member> result = repo.findById(memberHelperInfoDTO.getId());

        Member entity = result.get();
        if(entity.getState() == 1) {
            memberHelperInfoDTO.setState(2);
        } else {
            memberHelperInfoDTO.setState(1);
        }

        entity.changeState(memberHelperInfoDTO.getState());

        repo.save(entity);
    }

    //헬퍼 승인시에 userCheck 1로 변경
    @Override
    public void userCheck(MemberDto memberDto) {
        Optional<Member> result = repo.findById(memberDto.getId());

        Member entity = result.get();

        memberDto.setUserCheck(1);
        entity.changeUserCheck(memberDto.getUserCheck());

        repo.save(entity);
    }

}
