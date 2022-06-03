package com.careerdev.StocksApi.controllers;

import com.careerdev.StocksApi.models.Overview;
import com.careerdev.StocksApi.repositories.OverviewRepository;
import com.careerdev.StocksApi.utils.ApiError;
import com.careerdev.StocksApi.utils.Csv;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping ("/api/overview")
public class OverviewController {

    @Autowired
    private Environment env;

    @Autowired
    private OverviewRepository repository;

    String URL = "https://www.alphavantage.co/query?function=OVERVIEW";


    @GetMapping ("/{symbol}")
    public ResponseEntity<?> dynamicOverview (@PathVariable String symbol, RestTemplate restTemplate){

        try {

            String tickerURL = URL + "&symbol=" + symbol + "&apikey=" + env.getProperty ( "STOCK_API_KEY" );

            Overview response = restTemplate.getForObject ( tickerURL,Overview.class );

            if (response == null){

                ApiError.throwErr ( 500,"AV server did not respond" );

            } else if (response.getSymbol () == null){

                ApiError.throwErr ( 404,"Invalid Stock: " + symbol );

            }

            return ResponseEntity.ok (response);

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

    @PostMapping ("/{symbol}")
    public ResponseEntity<?> dynamicOverviewPost (@PathVariable String symbol, RestTemplate restTemplate){

        try {

            String tickerURL = URL + "&symbol=" + symbol + "&apikey=" + env.getProperty ( "STOCK_API_KEY" );

            Overview response = restTemplate.getForObject ( tickerURL,Overview.class );

            if (response == null){
                ApiError.throwErr ( 500,"AV server did not respond" );

            } else if (response.getSymbol () == null){

                ApiError.throwErr ( 404,"Invalid Stock: " + symbol );

            }

            Overview savedOverview = repository.save ( response );

            return ResponseEntity.ok (savedOverview);

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (DataIntegrityViolationException e ){

            return ApiError.customApiError ( "Can't Upload Duplicate Stock", 400 );

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

    @GetMapping ("/all")
    public ResponseEntity<?> getAllOverviews (){

        ArrayList<Overview> overviews = new ArrayList<> ();

        repository.findAll ().forEach ( overviews::add );

        if (overviews.isEmpty ()){

            return ResponseEntity.status ( 404 ).body ( "Database Is Empty" );

        }

        return ResponseEntity.ok (overviews);

    }

    @DeleteMapping ("/all")
    public ResponseEntity<?> deleteAllOverviews (){

        long count = repository.count ();

        if (repository.count () == 0)
            return ResponseEntity.ok ("Database Is Already Empty");

        repository.deleteAll ();

        return ResponseEntity.ok (count + " Overviews Have Been Deleted");

    }


    @GetMapping ("/id/{id}")
    public ResponseEntity<?> getOverviewById (@PathVariable String id){

        try{

            Optional<Overview> overview = repository.findById ( Long.parseLong ( id ) );

            if (overview.isEmpty ())
                ApiError.throwErr ( 404,"Invalid Stock ID: " + id  );

            return ResponseEntity.ok ( overview.get () );

            //return new ResponseEntity<Overview> ( repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.NOT_FOUND) ), HttpStatus.OK );

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (NumberFormatException e){

            return ApiError.customApiError ( "Invalid Id : " + id + " Must Be A Number", 400 );

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

    @GetMapping ("/symbol/{symbol}")
    public ResponseEntity<?> getOverviewBySymbol (@PathVariable String symbol){

        try{

            Optional<Overview> overview = repository.findBySymbol ( symbol );

            if (overview.isEmpty())
                ApiError.throwErr ( 404,"Invalid Stock: " + symbol  );

            return ResponseEntity.ok ( overview );

            //return new ResponseEntity<Overview> ( repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.NOT_FOUND) ), HttpStatus.OK );

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

    @GetMapping ("/name/{name}")
    public ResponseEntity<?> getOverviewByName (@PathVariable String name){

        try{

            Optional<Overview> overview = repository.findByName ( name );

            if (overview.isEmpty())
                ApiError.throwErr ( 404,"Invalid Name: " + name  );

            return ResponseEntity.ok ( overview );

            //return new ResponseEntity<Overview> ( repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.NOT_FOUND) ), HttpStatus.OK );

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

    @GetMapping ("/exchange/{exchange}")
    public ResponseEntity<?> getOverviewByExchange (@PathVariable String exchange){

        try{

            List<Overview> overview = repository.findByExchange ( exchange );

            if (overview.isEmpty())
                ApiError.throwErr ( 404,"Invalid Exchange: " + exchange );

            return ResponseEntity.ok ( overview );

            //return new ResponseEntity<Overview> ( repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.NOT_FOUND) ), HttpStatus.OK );

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

    @GetMapping ("/asset/{asset}")
    public ResponseEntity<?> getOverviewByAssetType (@PathVariable String asset){

        try{

            List<Overview> overview = repository.findByAssetType ( asset );

            if (overview.isEmpty())
                ApiError.throwErr ( 404,"Invalid Asset Type: " + asset );

            return ResponseEntity.ok ( overview );

            //return new ResponseEntity<Overview> ( repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.NOT_FOUND) ), HttpStatus.OK );

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

    @GetMapping ("/currency/{currency}")
    public ResponseEntity<?> getOverviewByCurrency (@PathVariable String currency){

        try{

            List<Overview> overview = repository.findByCurrency ( currency );

            if (overview.isEmpty())
                ApiError.throwErr ( 404,"Invalid Currency: " + currency );

            return ResponseEntity.ok ( overview );

            //return new ResponseEntity<Overview> ( repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.NOT_FOUND) ), HttpStatus.OK );

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

    @GetMapping ("/country/{country}")
    public ResponseEntity<?> getOverviewByCountry (@PathVariable String country){

        try{

            List<Overview> overview = repository.findByCountry ( country );

            if (overview.isEmpty())
                ApiError.throwErr ( 404,"Invalid Country: " + country );

            return ResponseEntity.ok ( overview );

            //return new ResponseEntity<Overview> ( repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.NOT_FOUND) ), HttpStatus.OK );

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

    @GetMapping ("/sector/{sector}")
    public ResponseEntity<?> getOverviewBySector (@PathVariable String sector){

        try{

            List<Overview> overview = repository.findBySector ( sector );

            if (overview.isEmpty())
                ApiError.throwErr ( 404,"Invalid Sector: " + sector );

            return ResponseEntity.ok ( overview );

            //return new ResponseEntity<Overview> ( repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.NOT_FOUND) ), HttpStatus.OK );

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }
    }

   // @PostMapping ("/fill")
//    public ResponseEntity<?> getCsvFile (RestTemplate restTemplate){
//
//        //Small batches of 5 search if exist in database first then if not add to repo
//        try {
//
//            //Code Flow testing
//            int count = 1;
//
//            String lURL = "https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=" + env.getProperty ( "STOCK_API_KEY" );
//
//            String test = restTemplate.getForObject ( lURL, String.class );
//
//          //  GsonJsonParser test3 = new GsonJsonParser ();
//
//
//            //Code Flow testing
//            System.out.println ( count++ );
//
//            assert test != null;
//            ArrayList<String> symbols = Csv.csvToArrayListByColumn ( test, 0, 100 );
//
//            //Code Flow testing
//            System.out.println ( count++ );
//
//            ArrayList<Overview> overviews = new ArrayList<> ();
//
////            ArrayList<String> overviews = new ArrayList<> ();
//
////            for (String s:
////                 symbols) {
////
////                    Overview ov = restTemplate.getForObject ( symbolUrl ( s ),Overview.class );
////
////                    if (ov.getSymbol () != null)
////                        overviews.add ( ov );
////            }
//
//            //Code Flow testing
//            System.out.println ( count++ );
//
////            for (String s : symbols) {
////                overviews.add ( dynamicOverview ( s,restTemplate ).getBody ());
////            }
//
//            //symbols.stream ().forEach ( symbol -> repository.save ( restTemplate.getForObject (  symbolUrl (symbol) , Overview.class ) ) );
//
//
//            int attempts = 0;
//
//            for (String symbol : symbols) {
//
//                if (repository.findBySymbol ( symbol ).isEmpty ()){
//
//                    String test4 = restTemplate.getForObject ( symbolUrl ( symbol ),String.class);
//                   // System.out.println (test3.parseList ( test4 ));
//                    Overview ov = restTemplate.getForObject ( symbolUrl ( symbol ), Overview.class );
//
//                    if (ov.getSymbol () != null){
//                        overviews.add ( ov );
//                        repository.save ( ov );
//
//                    }
//
//
//
//                }
//
//
//                //  String ov = restTemplate.getForObject ( symbolUrl ( symbol ), String.class );
//
//
//
////                if (attempts % 5 == 0) {
////                    try {
////                        TimeUnit.MINUTES.sleep ( 5 );
////                    } catch (InterruptedException e) {
////                        e.printStackTrace ();
////                    }
////                }
//
//                attempts++;
//
//
//
//
//                        /*Error while extracting response for type [class com.careerdev.StocksApi.models.Overview]
//                         and content type [application/json]; nested exception is org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error:
//                         Cannot deserialize value of type `long` from String "None": not a valid `long` value; nested exception is com.fasterxml.jackson.databind.exc.InvalidFormatException:
//                         Cannot deserialize value of type `long` from String "None": not a valid `long` valueat [Source: (org.springframework.util.StreamUtils$NonClosingInputStream); line: 15, column: 29]
//                         (through reference chain: com.careerdev.StocksApi.models.Overview["MarketCapitalization"])
//                           */
//
//                //5 calls per minute 500 cals a day
//
//                /*
//                 *  "{\n    \"Note\": \"Thank you for using Alpha Vantage! Our standard API call frequency is 5 calls per minute and 500 calls per day.
//                 * Please visit https://www.alphavantage.co/premium/ if you would like to target a higher API call frequency.\"\n}",*/
//            }
//
//            //Code Flow testing
//            System.out.println ( count++ );
//
//            // Overview response = restTemplate.getForObject ( tickerURL,Overview.class );
//
////            if (response == null){
////                ApiError.throwErr ( 500,"AV server did not respond" );
////
////            } else if (response.getSymbol () == null){
////
////                ApiError.throwErr ( 404,"Invalid Stock: " + symbol );
////
////            }
////(long) symbols.size () + " Stocks Have Been Added"
//
//            return ResponseEntity.ok ( overviews );
//
//        } catch (HttpClientErrorException e){
//
//            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());
//
//        } catch (DataIntegrityViolationException e ){
//
//            return ApiError.customApiError ( "Can't Upload Duplicate Stock", 400 );
//
//        }catch (Exception e){
//
//           return ApiError.genericApiError ( e );
//
//        }
//    }


    @DeleteMapping ("/id/{id}")
    public ResponseEntity<?> deleteByOverviewById (@PathVariable String id){

        try{

            Optional<Overview> overview = repository.findById ( Long.parseLong ( id ) );

            if (overview.isEmpty ())
                ApiError.throwErr ( 404,"Invalid Stock ID: " + id  );

            repository.delete ( overview.get () );

            return ResponseEntity.ok (overview.get ().getSymbol () + " has been deleted");

        } catch (HttpClientErrorException e){

            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());

        } catch (NumberFormatException e){

            return ApiError.customApiError ( "Invalid Id : " + id + " Must Be A Number", 400 );

        } catch (Exception e){

            return ApiError.genericApiError ( e );

        }

    }

//    @DeleteMapping ("/symbol/{symbol}")
//    public ResponseEntity<?> DeleteOverviewBySymbol (@PathVariable String symbol){
//
//        try{
//
//            Optional<Overview> overview = repository.findBySymbol ( symbol );
//
//            if (overview.isEmpty())
//                ApiError.throwErr ( 404,"Invalid Stock: " + symbol  );
//
//           Long test =  repository.removeBySymbol ( symbol );
//
//            return ResponseEntity.ok (  test + " Has Been Deleted" );
//
//            //return new ResponseEntity<Overview> ( repository.findById ( id ).orElseThrow (() -> new ResponseStatusException ( HttpStatus.NOT_FOUND) ), HttpStatus.OK );
//
//        } catch (HttpClientErrorException e){
//
//            return ApiError.customApiError ( e.getMessage (), e.getStatusCode ().value ());
//
//        } catch (Exception e){
//
//            return ApiError.genericApiError ( e );
//
//        }
//    }

    public String symbolUrl (String symbol){

        return URL + "&symbol=" + symbol + "&apikey=" + env.getProperty ( "STOCK_API_KEY" );

    }


}

//Get all and return array of overviews

//Delete all and return count of overviews deleted