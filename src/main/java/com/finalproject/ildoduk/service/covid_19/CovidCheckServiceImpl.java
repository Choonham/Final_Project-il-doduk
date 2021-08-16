package com.finalproject.ildoduk.service.covid_19;

import com.finalproject.ildoduk.dto.covid.DisasterMsgDTO;
import com.finalproject.ildoduk.dto.covid.TotalCovidDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class CovidCheckServiceImpl implements CovidCheckService{
    // 코로나 전체 통계 확인 (누적 확진자, 사망자, 누적 확진률, 최근 10일 누적 확진자 수(기준일 전일까지))

    DecimalFormat df = new DecimalFormat("#.###");

    public TotalCovidDTO totalCovid19Info() {

        TotalCovidDTO totalCovidDTO = new TotalCovidDTO();

        String serviceKey = "dLwRLlVhffDKhCUNxrsWHSM%2B1ARdyI1g4e9FQZXWJeel77rFSF3AMcT27JhOyTTUsaoZFhrlYWlneUOk9%2FEJkg%3D%3D";

        float accDefRate = 0; // 누적 확진률
        int accDeathCnt = 0; // 누적 사망자
        int accDecideCnt = 0; // 누적 확진자
        int deathCntToday = 0; // 기준일 사망자
        ArrayList<Integer> decideCnt = new ArrayList<>();

        try{

            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            Calendar cal = Calendar.getInstance();

            cal.setTime(date);
            cal.add(Calendar.DATE, 0);
            String today = yyyyMMdd.format(cal.getTime());

            cal.add(Calendar.DATE, -10);
            String tenDaysAgo = yyyyMMdd.format(cal.getTime());

            String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson?ServiceKey=" + serviceKey +
                    "&pageNo=1&numOfRows=10&startCreateDt=" + tenDaysAgo + "&endCreateDt=" + today;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);
            doc.getDocumentElement().normalize();

            Element e;

            NodeList parentNodeList = doc.getElementsByTagName("item");

            for(int i = 0; i < parentNodeList.getLength(); i++){

                if(i == 0){ // 기준일 확진률, 사망자, 누적 확진자 수를 구하기 위한 로직
                    e = (Element) parentNodeList.item(i);
                    String accDefRateStr = e.getElementsByTagName("accDefRate").item(0).getTextContent();
                    accDefRate = Float.parseFloat(accDefRateStr);

                    totalCovidDTO.setAccDefRate(df.format(accDefRate));

                    String accDeathCntStr = e.getElementsByTagName("deathCnt").item(0).getTextContent();
                    accDeathCnt = Integer.parseInt(accDeathCntStr);

                    totalCovidDTO.setAccDeathCnt(accDeathCnt);

                    String accDecideCntStr = e.getElementsByTagName("decideCnt").item(0).getTextContent();
                    accDecideCnt = Integer.parseInt(accDecideCntStr);

                    totalCovidDTO.setAccDecideCnt(accDecideCnt);

                } else if(i == 1){ // 일일 확진자, 사망자 수를 구하기 위한 로직
                    e = (Element) parentNodeList.item(i);
                    String decideCntStr = e.getElementsByTagName("decideCnt").item(0).getTextContent();
                    decideCnt.add(accDecideCnt - Integer.parseInt(decideCntStr));

                    String deathCntStr = e.getElementsByTagName("deathCnt").item(0).getTextContent();
                    deathCntToday = accDeathCnt - Integer.parseInt(deathCntStr);

                    totalCovidDTO.setDeathCntToday(deathCntToday);

                } else {
                    e = (Element) parentNodeList.item(i);
                    Element eBefore = (Element) parentNodeList.item(i-1);

                    String decideCntStr = e.getElementsByTagName("decideCnt").item(0).getTextContent();
                    String decideCntBeforeStr = eBefore.getElementsByTagName("decideCnt").item(0).getTextContent();

                    decideCnt.add(Integer.parseInt(decideCntBeforeStr) - Integer.parseInt(decideCntStr));
                }
            }

            ArrayList<Integer> decideCntDays = new ArrayList<>();

            for(int i = 0; i < decideCnt.size(); i++){
                decideCntDays.add(decideCnt.get(i));
            }

            totalCovidDTO.setDecideCntDays(decideCntDays);

        } catch (Exception e){
            e.printStackTrace();
        }

        return totalCovidDTO;
    }

    // 재난문자
    public DisasterMsgDTO dailyCovid19Info(String keyword, String sido) {

        DisasterMsgDTO disasterMsgDTO = null;

        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

        Date date = new Date();
        String today = yyyyMMdd.format(date);

        ArrayList<String> resultArray = new ArrayList<>();
        Set<String> sidoList = new HashSet<>();

        HashMap<String, ArrayList<String>> resultMap = new HashMap<>();

        try{
            String covidCheckUrl = "https://m.news.naver.com/covid19/live.nhn?date="+today;
            org.jsoup.nodes.Document doc = Jsoup.connect(covidCheckUrl).get();
            org.jsoup.nodes.Element element = doc.select("script").get(6);
            if(element.data().contains("smsList")){
                Pattern pattern = Pattern.compile("smsList = ([^;]{3,});");
                Matcher matcher = pattern.matcher(element.data());
                if (matcher.find()) {
                    ArrayList<String> totalArray = new ArrayList<>();

                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(matcher.group(1));
                    JSONObject result = (JSONObject) obj;
                    JSONObject totalList = (JSONObject) result.get("result");
                    JSONArray list = (JSONArray) totalList.get("list");

                    for(Object o : list) {
                        JSONObject jObj = (JSONObject) o;
                        String tempSido = jObj.get("sido").toString();
                        String msg = jObj.get("message").toString();

                        sidoList.add(tempSido);

                        if(keyword == null){
                            if(resultMap.containsKey(tempSido)){
                                ArrayList<String> tempList = resultMap.get(tempSido);
                                tempList.add(msg);
                                resultMap.put(tempSido, tempList);
                            } else{
                                ArrayList<String> tempList = new ArrayList<>();
                                tempList.add(msg);
                                resultMap.put(tempSido, tempList);
                            }
                            totalArray.add(msg);
                            resultMap.put("total", totalArray);
                        } else {
                            if(msg.contains(keyword)){
                                if(resultMap.containsKey(tempSido)){
                                    ArrayList<String> tempList = resultMap.get(tempSido);
                                    tempList.add(msg);
                                    resultMap.put(tempSido, tempList);
                                } else{
                                    ArrayList<String> tempList = new ArrayList<>();
                                    tempList.add(msg);
                                    resultMap.put(tempSido, tempList);
                                }
                                totalArray.add(msg);
                                resultMap.put("total", totalArray);
                            }
                        }
                    }

                    if(sido == null){
                        resultArray = resultMap.get("total");
                    } else {
                        resultArray = resultMap.get(sido);
                    }

                    disasterMsgDTO = DisasterMsgDTO.builder().msgList(resultArray).sidoList(sidoList).build();

                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return disasterMsgDTO;
    }

    // 백신 접종 현황
    public HashMap<String, Integer[]> vaccineInfo() {


        HashMap<String, Integer[]> resultMap = new HashMap<>();

        try{
            String url = "https://nip.kdca.go.kr/irgd/cov19stats.do?list=sido";

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);
            doc.getDocumentElement().normalize();

            Element e;

            NodeList parentNodeList = doc.getElementsByTagName("item");

            for(int i = 0; i < parentNodeList.getLength(); i++) {
                e =  (Element) parentNodeList.item(i);
                String sido = e.getElementsByTagName("sidoNm").item(0).getTextContent();

                Integer[] resultArray = new Integer[4];

                resultArray[0] = Integer.parseInt(e.getElementsByTagName("firstCnt").item(0).getTextContent());
                resultArray[1] = Integer.parseInt(e.getElementsByTagName("firstTot").item(0).getTextContent());
                resultArray[2] = Integer.parseInt(e.getElementsByTagName("secondCnt").item(0).getTextContent());
                resultArray[3] = Integer.parseInt(e.getElementsByTagName("secondTot").item(0).getTextContent());

                resultMap.put(sido, resultArray);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }
}
