package com.careerdev.StocksApi.controllers;

import com.careerdev.StocksApi.models.Overview;
import com.careerdev.StocksApi.repositories.OverviewRepository;
import com.careerdev.StocksApi.utils.ApiErrorHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

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
    @PostMapping("/test")
    public ResponseEntity<?> testDbOverview(RestTemplate restTemplate){

        try {

            String testURL = URL +  "&symbol=IBM&apikey=demo";

            Overview response = restTemplate.getForObject ( testURL, Overview.class );

            if (response == null){

                return ApiErrorHandling.customApiError ( "AV server did not respond", HttpStatus.INTERNAL_SERVER_ERROR );

            } else if (response.getSymbol () == null){

                return ApiErrorHandling.customApiError ( "No Data recieved", HttpStatus.NOT_FOUND );

            }

            Overview savedOverview = repository.save ( response );

            return ResponseEntity.ok (savedOverview);

        }

        catch (DataIntegrityViolationException e ){

            return ApiErrorHandling.customApiError ( "Can't Upload Duplicate Stock", HttpStatus.BAD_REQUEST );
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

    @PostMapping ("/{symbol}")
    public ResponseEntity<?> dynamicOverviewPost (@PathVariable String symbol, RestTemplate restTemplate){

        try {

            String tickerURL = URL + "&symbol=" + symbol + "&apikey=" + env.getProperty ( "STOCK_API_KEY" );

            Overview response = restTemplate.getForObject ( tickerURL,Overview.class );

            if (response == null){

                return ApiErrorHandling.customApiError ( "AV server did not respond", HttpStatus.INTERNAL_SERVER_ERROR );

            } else if (response.getSymbol () == null){

                return ApiErrorHandling.customApiError ( "No Data recieved" + symbol, HttpStatus.NOT_FOUND );

            }

            Overview savedOverview = repository.save ( response );

            return ResponseEntity.ok (savedOverview);

        }

        catch (DataIntegrityViolationException e ){

            return ApiErrorHandling.customApiError ( "Can't Upload Duplicate Stock", HttpStatus.BAD_REQUEST );
        }

        catch (Exception e){

            return ApiErrorHandling.genericApiError ( e );

        }
    }

    @GetMapping ("/all")
    public ResponseEntity<?> getAllOverviews (){

        ArrayList<Overview> overviews = new ArrayList<> ();

        repository.findAll ().forEach ( overviews::add );

        if (overviews.isEmpty ()){

            return ResponseEntity.ok ("The Database Is Empty");

        }

        return ResponseEntity.ok (overviews);

    }

    @DeleteMapping ("/all")
    public ResponseEntity<?> deleteAllOverviews (){

        long count = repository.count ();

        repository.deleteAll ();

        return ResponseEntity.ok (count + " Overviews Have Been Deleted");

    }


    @GetMapping ("/id/{id}")
    public ResponseEntity<?> getOverviewById (@PathVariable long id){

        try{

            //figure how to send body from here
            return new ResponseEntity<Overview> ( repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.NOT_FOUND) ), HttpStatus.OK );

        } catch (Exception e){

            return ApiErrorHandling.genericApiError ( e );
        }
    }


    @DeleteMapping ("/id/{id}")
    public ResponseEntity<?> deleteByOverviewById (@PathVariable long id){

        try{

            Overview overview = repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.BAD_REQUEST ));

            String deleteMessage = "{ID: " + overview.getId () + "| Symbol: " + overview.getSymbol () + "} Has Been Deleted";

            repository.delete ( overview );

            return ResponseEntity.ok (deleteMessage);

        } catch (Exception e){

            return ApiErrorHandling.genericApiError ( e );

        }

    }

}

//Get all and return array of overviews

//Delete all and return count of overviews deleted