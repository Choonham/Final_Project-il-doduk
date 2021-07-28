package com.finalproject.ildoduk.service.member.serviceImpl;

import com.finalproject.ildoduk.dto.member.MemberDto;

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
            dto.setGender(gender);

            System.out.println("id::::  " + dto.getId());
            System.out.println("gender::::  " + dto.getGender());
            System.out.println("nickname:::   " + dto.getNickname());


        }catch(ParseException e){
            System.out.println("Json parse err" + e);
        }
        return dto;
    }





}
