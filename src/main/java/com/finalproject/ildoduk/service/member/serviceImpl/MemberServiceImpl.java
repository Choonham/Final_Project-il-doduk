package com.finalproject.ildoduk.service.member.serviceImpl;

import com.finalproject.ildoduk.dto.member.MemberDto;

import com.finalproject.ildoduk.dto.pay.PaymentDTO;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.repository.member.MemberRepository;
import com.finalproject.ildoduk.service.member.service.MemberService;
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
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository repo;

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

    @Override
    public void userRegister(MemberDto dto) {

        repo.save(dtoToEntity(dto));

    }

    public MemberDto userIdCheck(String id) {

        Optional<Member> result = repo.findById(id);

        log.info(" userIdDtoInit result ::::::" + result);

        return result.isPresent() ? EntityToDto(result.get()) : null;

    }

    @Override
    public MemberDto userIdDtoInit(MemberDto dto) {
        Optional<Member> result = repo.findById(dto.getId());

        log.info(" userIdDtoInit result ::::::" + result.get());

        return result.isPresent() ? EntityToDto(result.get()) : null;
    }

    @Override
    public MemberDto userIdPwdCheck(String id, String pwd) {

        Optional<Member> result = repo.findByIdAndPwd(id,pwd);

        return result.isPresent()? EntityToDto(result.get()) : null;

    }


    @Override
    public void userModify(MemberDto dto) {
        Optional<Member> result = repo.findById(dto.getId());

        if(result.isPresent()){

            Member entity = result.get();

/*
            entity.changeAddress(dto.getAddress());
            entity.changePhoto(dto.getPhoto());
            entity.changeIntro(dto.getIntro());
*/

            repo.save(entity);
        }
    }

    @Override
    public void userDelete(String id) {
        repo.deleteById(id);

    }

    @Override
    public MemberDto kakaoLogin(String json)  {
        MemberDto ud = new MemberDto();
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        JSONObject jsonObj2;

        MemberDto dto = new MemberDto();

        try{
            jsonObj =(JSONObject)parser.parse(json);

            String email = (String)jsonObj.get("email");
            String gender = (String)jsonObj.get("gender");
            String profile = jsonObj.get("profile").toString(); //profile 은 값안에 키, 값이 또 있어서 한번 쪼개고
            jsonObj2 = (JSONObject)parser.parse(profile);//한번더 쪼개기
            String nickname = (String)jsonObj2.get("nickname");

            dto.setId(email);
            dto.setNickname(nickname);
            if(gender.equals("male")){
            dto.setGender("남");
            }else{
                dto.setGender("여");
            }
            System.out.println(" kakaoLogin :: dto get id::::  " + dto.getId());
            System.out.println(" kakaoLogin :: dto get gender::::  " + dto.getGender());
            System.out.println(" kakaoLogin :: dto get nickname:::   " + dto.getNickname());


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

    //경매 성공시에 사용자 포인트 차감
    //판매자가 글을 올리고, 헬퍼가 경매 참여 -> 일을 끝냈을 경우에만
    //일을 끝냈다는 버튼을 눌렀을 경우에만 사용자 포인트 차감(그대로), 헬퍼의 경우 친절점수에 따라 수수료 부여해서 포인트 증가,
    @Override
    public void minusPonit(MemberDto dto) {

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


    // 중개 수수료 기본 : 10%  -> 0.9
    //    우대 수수료 : 7% -> 0.93
    // 남은 금액 Admin계정으로
    @Override
    public void plusPoint(MemberDto dto) {
        log.info("헬퍼쪽 포인트 업데이트 시작");

        String userID = dto.getId();
        //들어온 포인트 여기서 조건을 통하여 2가지로 분리 User 리뷰 확인
        // Member의 친절 점수로 : 5점 만점에 3.5이상일 경우 우대 수수료 적용??

        int point = dto.getPoint();
        int total = 0;

        //친절 점수 들어갈 곳
        String test = null;
        if(test == null){
            //우대 수수료 적용
            total = (int)(Math.ceil(point * 0.93));

        } else {
            total = (int)(Math.ceil(point * 0.9));
        }

        Optional<Member> result = repo.findById(userID);

        if(result.isPresent()){
            Member entity = result.get();
            log.info("헬퍼쪽 포인트 업데이트 진행중");

            repo.pointUpdate(total, entity.getId());
        }
    }

}
