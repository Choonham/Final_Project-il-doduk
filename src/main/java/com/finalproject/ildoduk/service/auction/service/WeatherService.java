package com.finalproject.ildoduk.service.auction.service;

import com.finalproject.ildoduk.dto.auction.*;

import java.util.*;

public interface WeatherService {

    ArrayList<WeatherDTO> weather(String zone, String day);

}
