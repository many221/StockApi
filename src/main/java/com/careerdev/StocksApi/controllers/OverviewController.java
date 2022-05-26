package com.careerdev.StocksApi.controllers;

import com.careerdev.StocksApi.models.Overview;
import com.careerdev.StocksApi.repositories.OverviewRepository;
import com.careerdev.StocksApi.utils.ApiErrorHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping ("/api/overview")
public class OverviewController {

    @Autowired
    private Environment env;

    @Autowired
    private OverviewRepository repository;

    String URL = "https://www.alphavantage.co/query?function=OVERVIEW";

    @GetMapping ("/test")
    public ResponseEntity<?> testOverview(RestTemplate restTemplate){

        try {

            String testURL = URL +  "&symbol=IBM&apikey=demo";

            Overview response = restTemplate.getForObject ( testURL, Overview.class );

            Overview savedOverview = repository.save ( response );

            return ResponseEntity.ok (response);

        }
        catch (IllegalArgumentException e){

            return ApiErrorHandling.customApiError ( "Check Url", HttpStatus.INTERNAL_SERVER_ERROR );

        }
        catch (Exception e){

            return ApiErrorHandling.genericApiError ( e );

        }
    }

    //test to db
    @GetMapping ("/test")
    public ResponseEntity<?> testDbOverview(RestTemplate restTemplate){

        try {

            String testURL = URL +  "&symbol=IBM&apikey=demo";

            Overview response = restTemplate.getForObject ( testURL, Overview.class );

            if (response == null){

                return ApiErrorHandling.customApiError ( "AV server did not respond", HttpStatus.INTERNAL_SERVER_ERROR );

            } else if (response.getSymbol () == null){

                return ApiErrorHandling.customApiError ( "No Data recieved", HttpStatus.NOT_FOUND );

            }


            return ResponseEntity.ok (response);

        }
        catch (IllegalArgumentException e){

            return ApiErrorHandling.customApiError ( "Check Url", HttpStatus.INTERNAL_SERVER_ERROR );

        }
        catch (Exception e){

            return ApiErrorHandling.genericApiError ( e );

        }
    }




    @GetMapping ("/{symbol}")
    public ResponseEntity<?> dynamicOverview (@PathVariable String symbol, RestTemplate restTemplate){

        try {

            String tickerURL = URL + "&symbol=" + symbol + "&apikey=" + env.getProperty ( "STOCK_API_KEY" );

            Overview response = restTemplate.getForObject ( tickerURL,Overview.class );

            if (response == null){

                return ApiErrorHandling.customApiError ( "AV server did not respond", HttpStatus.INTERNAL_SERVER_ERROR );

            } else if (response.getSymbol () == null){

                return ApiErrorHandling.customApiError ( "No Data recieved" + symbol, HttpStatus.NOT_FOUND );

            }

            return ResponseEntity.ok (response);

        } catch (Exception e){

            return ApiErrorHandling.genericApiError ( e );

        }
    }


}
