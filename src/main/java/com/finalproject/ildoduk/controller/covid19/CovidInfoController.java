package com.finalproject.ildoduk.controller.covid19;

import com.finalproject.ildoduk.dto.covid.DisasterMsgDTO;
import com.finalproject.ildoduk.dto.covid.TotalCovidDTO;
import com.finalproject.ildoduk.service.covid_19.CovidCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
@RequestMapping("/covidInfo")
@Log4j2
@RequiredArgsConstructor
public class CovidInfoController {

    private final CovidCheckService covidCheckService;

    @GetMapping("/covidInfo")
    public void getCovidInfo(String keyword, String sido, Model model, HttpSession session) {
        TotalCovidDTO totalCovid19InfoResult = covidCheckService.totalCovid19Info();

        HashMap<String, Integer[]> vaccineInfoResult = covidCheckService.vaccineInfo();

        DisasterMsgDTO dailyCovid19InfoResult = covidCheckService.dailyCovid19Info(keyword, sido);

        model.addAttribute("totalCovid19InfoResult", totalCovid19InfoResult);
        model.addAttribute("vaccineInfoResult", vaccineInfoResult);
        model.addAttribute("dailyCovid19InfoResult", dailyCovid19InfoResult);
    }

    @GetMapping("/searchMsg")
    @ResponseBody
    public DisasterMsgDTO searchMsg(@RequestParam("keyword") String keyword, @RequestParam("sido") String sido) {
        DisasterMsgDTO dailyCovid19InfoResult;

        if(sido.equals("total")){
           dailyCovid19InfoResult = covidCheckService.dailyCovid19Info(keyword, null);
        } else {
            dailyCovid19InfoResult = covidCheckService.dailyCovid19Info(keyword, sido);
        }
        return dailyCovid19InfoResult;
    }

}
