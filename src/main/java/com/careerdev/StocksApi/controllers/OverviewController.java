package com.careerdev.StocksApi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/overview")
public class OverviewController {

    @Autowired
    private Environment env;

    String URL = "https://www.alphavantage.co/query?function=OVERVIEW";

    @GetMapping("/test")
    public ResponseEntity<?> testOverview(RestTemplate restTemplate){

        try {

            String testURL = URL +  "&symbol=IBM&apikey=demo";

            Object response = restTemplate.getForObject ( testURL, Object.class );

            return ResponseEntity.ok (response);

        } catch (Exception e){

            return ResponseEntity.internalServerError ().body ( e.getMessage () );

        }
    }


    @GetMapping("/{symbol}")
    public ResponseEntity<?> dynamicOverview (@PathVariable String symbol, RestTemplate restTemplate){

        try {

            String tickerURL = URL + "&symbol=" + symbol + "&apikey=" + env.getProperty ( "STOCK_API_KEY" );

            Object response = restTemplate.getForObject ( tickerURL,Object.class );

            return ResponseEntity.ok (response);

        } catch (Exception e){

            return ResponseEntity.internalServerError ().body ( e.getMessage () );

        }
    }


}
