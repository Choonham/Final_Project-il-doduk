package com.finalproject.ildoduk.service.auction;

import com.finalproject.ildoduk.dto.*;
import com.finalproject.ildoduk.dto.auction.*;
import com.finalproject.ildoduk.dto.member.MemberDto;
import com.finalproject.ildoduk.entity.auction.AuctionList;
import com.finalproject.ildoduk.entity.auction.BiddingList;
import com.finalproject.ildoduk.entity.member.Member;
import com.finalproject.ildoduk.repository.auction.AuctionListRepository;
import com.finalproject.ildoduk.repository.auction.BiddingListRepository;
import com.finalproject.ildoduk.repository.member.MemberRepository;
import com.finalproject.ildoduk.service.auction.service.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.web.bind.annotation.*;

import javax.jdo.annotations.Transactional;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class auctionServieceTest {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    BiddingListRepository repository;

    @Autowired
    MemberRepository mem_repo;
    @Autowired
    BiddingListRepository biddingListRepository;
    @Autowired
    AuctionListRepository auc_repo;

    @Test
    public void testGetMyBids() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResultsDTO<AuctionBiddingDTO, Object[]> result = auctionService.getMyBids(pageRequestDTO, "godnjs729417@naver.com", true);
        System.out.println(result.getDtoList().size());

        for (AuctionBiddingDTO DTO : result.getDtoList()) {
            System.out.println(DTO);
        }
    }


    @Test
    public void testRegister() {
        for (int i = 0; i < 10; i++) {
            AuctionListDTO dto = AuctionListDTO.builder().auctionGap(100).category(1).content("test Dummy " + i).doDateTime(LocalDateTime.of(2022, 01, 01, 0, 0))
                    .user("vhdvhd0307@naver.com").level(1).predictHour("2").title("test " + i).startPrice(20000).address("testteesteetste").sido("일").sigungu("이").build();

            Long aucSeq = auctionService.register(dto);
        }
    }

    @Test
    public void testBiddingIn() {

        Optional<BiddingList> op = repository.findById(1L);
        op.get().getAucSeq().getAddress();


    }

    @Test //중기예보정보 받아오기
    public void test() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/MidFcstInfoService/getMidFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=서비스키"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + URLEncoder.encode("jgDiflmdZUP%2Bvl6mm9XxQSOH2GMep%2BH0mza4OC%2BLS6%2FsWCGAzxCy6BGnaQBqDRKWy1kTul7YnMuK4vxwNMAqVw%3D%3D", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("stnId", "UTF-8") + "=" + URLEncoder.encode("108", "UTF-8")); /*108 전국, 109 서울, 인천, 경기도 등 (활용가이드 하단 참고자료 참조)*/
        urlBuilder.append("&" + URLEncoder.encode("tmFc", "UTF-8") + "=" + URLEncoder.encode("201310170600", "UTF-8")); /*-일 2회(06:00,18:00)회 생성 되며 발표시각을 입력 YYYYMMDD0600 (1800)-최근 24시간 자료만 제공*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
    }

    @Test //RSS 정보 받아오기 - 지역별 실시간 날씨 정보(3일)
    public void test2() {
        String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp";

        //받는데이터 존, 날짜

        try {
            Document doc = Jsoup.connect(url).data("zone", "1168020700").get();

            Elements el = doc.select("data");
            String location = doc.select("category").text();
            System.out.println("=================> " + location);
            ArrayList<WeatherDTO> today = new ArrayList<>();

            for (int i = 0; i < el.size(); i++) {
                Element e = el.get(i);
                String day = e.select("day").text();

                if ("1".equals(day)) {

                    WeatherDTO weatherDTO = new WeatherDTO();

                    String hour = e.select("hour").text();
                    String wfKor = e.select("wfKor").text();
                    String temp = e.select("temp").text();
                    String pop = e.select("pop").text();

                    weatherDTO.setDay(day);
                    weatherDTO.setHour(hour);
                    weatherDTO.setWfKor(wfKor);
                    weatherDTO.setTemp(temp);
                    weatherDTO.setPop(pop);

                    System.out.println(weatherDTO.toString());

                    today.add(weatherDTO);
                }
            }
        } catch (Exception e) {
            System.out.println("err=======" + e);
        }

    }

/*    @Test //RSS 정보 받아오기 - 지역별 날씨정보 (3일 ~ 10일)
    public void test3() {
        String url = "http://www.weather.go.kr/weather/forecast/mid-term-rss3.jsp";


        //stnId값 받아 올 것

        try {
            Document doc = Jsoup.connect(url).data("stnId", "131").get();
            Elements el = doc.select("location");

            //지역별 날씨 정보
            //날씨 정보 arrayList - 꺼낼 때, 도시별 날짜순으로 꺼내야함..
            ArrayList<WeatherDTO> weatherList = new ArrayList<>();

            for (int i = 0; i < el.size(); i++) {

                Element e = el.get(i);
                String province = e.select("province").text();
                String city = e.select("city").text();

                //city 가능 - 입력값

                Elements el2 = e.select("data");

                for (int j = 0; j < el2.size(); j++) {

                    Element e2 = el2.get(j);
                    String dateTime = e2.select("tmEf").text();
                    String mode = e2.select("mode").text();
                    String wf = e2.select("wf").text();
                    String tmn = e2.select("tmn").text();
                    String tmx = e2.select("tmx").text();
                    String rnSt = e2.select("rnSt").text();


                    WeatherDTO weather = new WeatherDTO();
                    weather.setLocation(province);
                    weather.setCity(city);
                    weather.setDateTime(dateTime);
                    weather.setTmn(tmn);
                    weather.setTmx(tmx);
                    weather.setWf(wf);
                    weather.setRnSt(rnSt);
                    if (mode.equals("A01")) {
                        weather.setDay(true);
                    } else {
                        weather.setDay(false);
                    }
                    System.out.println(weather.toString());

                    weatherList.add(weather);
                }
            }
        } catch (Exception e) {
            System.out.println("err=======" + e);
        }

    }*/


}
