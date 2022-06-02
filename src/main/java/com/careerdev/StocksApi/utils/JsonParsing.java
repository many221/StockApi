package com.careerdev.StocksApi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;

//@RestController
//@RequestMapping("/api/testing")
public class JsonParsing {

    public static String url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=demo";

//    @GetMapping
    public static ResponseEntity<?> testingParsing (RestTemplate restTemplate){

        try {

            String json = restTemplate.getForObject ( url,String.class );

//            JsonParsing parser = new JsonParsing ();
//
//            parser.url.lines ().forEach ( System.out::println );

            System.out.println (json);

            return ResponseEntity.ok (json);

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

    public static void main(String[] args) {

//        URI url2 = URI.create ("https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=demo");
//
//        String test =
//
//        JsonParsing parser = new JsonParsing ();
//
//
//        parser.url.lines ().forEach ( System.out::println );

        RestTemplate restTemplate = new RestTemplate ();

        testingParsing ( restTemplate );

    }

}
