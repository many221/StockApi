package com.careerdev.StocksApi.utils;

import com.careerdev.StocksApi.models.Overview;
import org.springframework.web.client.RestTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Scrapping {

    public static String URL_AA = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=demo";

    public static Overview getOverview(String url){

        RestTemplate restTemplate = new RestTemplate ();

        try {

            Overview testOv = restTemplate.getForObject ( url,Overview.class );

            System.out.println ( testOv.getSymbol () +" Has Been Taken From AlphaVantage");

            return testOv;

        } catch (Exception e){

            e.printStackTrace ();

            return null;

        }


    }

    public static String testOverview (String url){

        RestTemplate restTemplate = new RestTemplate ();

        try {

            String testOv = restTemplate.getForObject ( url, String.class );

            return testOv;

        } catch (Exception e){
            e.printStackTrace ();
            return null;
        }

    }

    public static void insertOverview(Overview ov){

        MysqlConnection mysqlConnect = new MysqlConnection();
        Connection conn = mysqlConnect.connect ();

        try {

            String sql = "insert into overview (id,asset_type, country, currency, dividend_date," +
                    " exchange, industry, market_cap, name, " +
                    "sector, symbol, year_high, year_low)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = conn.prepareStatement(sql);

            //X Figure Out Where To Increment ID
            preparedStmt.setLong ( 1,ov.getId ());
            preparedStmt.setString ( 2,ov.getAssetType ());
            preparedStmt.setString ( 3,ov.getCountry ());
            preparedStmt.setString ( 4,ov.getCurrency ());
            preparedStmt.setString ( 5,ov.getDividendDate ());
            preparedStmt.setString ( 6,ov.getExchange ());
            preparedStmt.setString ( 7,ov.getIndustry ());
            preparedStmt.setLong ( 8,ov.getMarketCap ());
            preparedStmt.setString ( 9,ov.getName ());
            preparedStmt.setString ( 10,ov.getSector ());
            preparedStmt.setString ( 11,ov.getSymbol ());
            preparedStmt.setFloat ( 12,ov.getYearHigh ());
            preparedStmt.setFloat ( 13,ov.getYearLow ());

            preparedStmt.executeUpdate ();

            System.out.println ( ov.getSymbol () + " Has Been Added To The DataBase");

        } catch (SQLException e) {
            e.printStackTrace ();
        }finally {
            mysqlConnect.disconnect();
        }


    }

    public static void clearDatabase(){

        MysqlConnection mysqlConnect = new MysqlConnection();
        Connection conn = mysqlConnect.connect ();

        try {

            String sql = "delete from overview";

            Statement statement = conn.createStatement ();

            statement.execute ( sql );

            System.out.println ("Database has been cleared");

        } catch (SQLException e) {
            e.printStackTrace ();
        }finally {
            mysqlConnect.disconnect();
        }
    }

    public static void getAndInstertOverView( String url){

        insertOverview ( Objects.requireNonNull ( getOverview ( url ) ) );

    }


    public static void getBatchOfOverviews (ArrayList<String> arr, String key) throws InterruptedException {

            //think of the logic for this
            //figure out how to add the remainders into the last group
            // split array based on how many api keys are given
            //or set limit per key
        int count = 1;
//        System.out.println (2);
        for (String symbol: arr) {

            String url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + symbol + "&apikey=" + key;
//            System.out.println (3);
            if (count%6!=0){
                            getAndInstertOverView ( url );
//                System.out.println (4);
                System.out.println (symbol);
            }else{
//                System.out.println (5);
                Thread.sleep ( 6000000 );
            }
            count++;
        }

    }


    public static void main(String[] args) throws InterruptedException {

//        switch (args[0]) {
//            case "insert" -> getAndInstertOverView ( URL_AA );
//            case "clear" -> clearDatabase ();
//            default -> System.out.println ( "Invalid option" );
//        }

        String myFile = "src/main/resources/companySymbols.csv";
        ArrayList<String> testArr = Csv.csvToArraylist ( myFile );

        int startIndex =  Integer.parseInt ( args[0] );

        //TODO Check if end index is > length of arrray if it is change end index to array length;

        assert testArr != null;
        getBatchOfOverviews ( (ArrayList<String>) testArr.subList (startIndex,startIndex+500 ),"heeeheee" );
        System.out.println (2);

    }
}
