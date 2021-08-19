package com.finalproject.ildoduk.service.auction.serviceImpl;

import com.finalproject.ildoduk.dto.auction.*;
import com.finalproject.ildoduk.entity.auction.*;
import com.finalproject.ildoduk.repository.auction.*;
import com.finalproject.ildoduk.service.auction.service.*;
import com.querydsl.core.*;
import com.querydsl.core.types.dsl.*;
import lombok.*;
import lombok.extern.log4j.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherCodeRepository weatherCodeRepository;

    @Override
    public String findZone(String sidoP, String sigunguP){

        String code = null;

        BooleanBuilder builder = new BooleanBuilder();
        QWeatherCode qWeatherCode = QWeatherCode.weatherCode;
        BooleanExpression sido = qWeatherCode.sido.contains(sidoP);
        BooleanExpression sigungu = qWeatherCode.sigungu.matches(sigunguP);

        BooleanExpression exAll = sido.and(sigungu);
        builder.and(exAll);

        try {
            WeatherCode weatherCode = weatherCodeRepository.findOne(builder).get();
            code = weatherCode.getCode();
        }catch (Exception e){
            code = null;
        }
        System.out.println("code =================== " + code );

        return code;

    }

    @Override
    public ArrayList<WeatherDTO> weather(String zone, String date){
        String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp";

        //받는데이터 존, 날짜(0 - 오늘 / 1-내일 / 2-모레)
        ArrayList<WeatherDTO> weatherList = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).data("zone", zone).get();
            Elements el = doc.select("data");
            String location = doc.select("title").text();

            for (int i = 0; i < el.size(); i++) {
                Element e = el.get(i);
                String day = e.select("day").text();


                if (date.equals(day)) {

                    WeatherDTO weatherDTO = new WeatherDTO();

                    String hour = e.select("hour").text(); //시간
                    String wfKor = e.select("wfKor").text(); //날씨
                    String temp = e.select("temp").text();  //온도
                    String pop = e.select("pop").text(); //강수확률

                    weatherDTO.setDay(day);
                    weatherDTO.setHour(hour);
                    weatherDTO.setWfKor(wfKor);
                    weatherDTO.setTemp(temp);
                    weatherDTO.setPop(pop);

                   // System.out.println(weatherDTO.toString());

                    weatherList.add(weatherDTO);
                }
            }
        } catch (Exception e) {
            System.out.println("err=======" + e);
        }
        return weatherList;
    }

}
