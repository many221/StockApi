package com.careerdev.StocksApi.utils;

import java.io.*;
import java.util.ArrayList;

public class Csv {

    // Takes A String of CSV data then parses it then returns it as an array list

    //TODO Get rid of Stocks with Dashes ✅
    //TODO reader and writer ✅
    //TODO Write parsed data to companySymbols file ✅
    //TODO Create new methods for where to start and stop based off of companySymbolCSV

    //O Blank space at beginning and ending of array


    public static ArrayList<String> csvToArrayListWithLimit(String csv, int column) {

        ArrayList<String> strArr = new ArrayList<> ();

        csv.lines ().skip ( 1 ).forEach ( s -> {

            String symbol = s.split ( "," )[column];

            if (!symbol.contains ( "-" ))
                strArr.add ( symbol );

        } );

        return strArr;
    }

    public static ArrayList<String> csvToArraylist(String path ){

        ArrayList<String> file = new ArrayList<> ();

        try {

            BufferedReader reader = new BufferedReader(new FileReader ( path ) );

            reader.lines ().skip ( 1 ).forEach ( file::add );

            reader.close ();

            return file;

        }catch (IOException e){

            e.printStackTrace ();

            return null;

        }


    }

    public static String csvToString(String path){

        StringBuilder file = new StringBuilder ();

        try {

            BufferedReader br = new BufferedReader ( new FileReader ( path ) );

            br.lines ().forEach (s -> file.append ( s ).append ( "\n" ));

            return file.toString ();

        }catch (IOException e){

            e.printStackTrace ();

            return e.toString ();

        }
    }

    public static void writer (String path, String str){

        try {

            FileWriter file = new FileWriter ( path,true );

            BufferedWriter writer = new BufferedWriter ( file );

            writer.write ( str );

            writer.newLine ();

            writer.close ();

        } catch (IOException e) {

            e.printStackTrace ();

        }

    }

    public static void clearFile (String path){

        try {

            FileWriter file = new FileWriter ( path);

            BufferedWriter writer = new BufferedWriter ( file );

            writer.write ( "" );

            writer.newLine ();

            writer.close ();

        } catch (IOException e) {

            e.printStackTrace ();

        }

    }



    public static void main(String[] args) {

        String test =
                "symbol,name,exchange,assetType,ipoDate,delistingDate,status\n" +
                "A,Agilent Technologies Inc,NYSE,Stock,1999-11-18,null,Active\n" +
                "AA,Alcoa Corp,NYSE,Stock,2016-10-18,null,Active\n" +
                "AAA,AAF FIRST PRIORITY CLO BOND ETF ,NYSE ARCA,ETF,2020-09-09,null,Active\n" +
                "AAAU,Goldman Sachs Physical Gold ETF,BATS,ETF,2018-08-15,null,Active\n" +
                "AAC,Ares Acquisition Corporation - Class A,NYSE,Stock,2021-03-25,null,Active\n"+
                "AAC-U,Ares Acquisition Corporation - Units (1 Ord Share Class A & 1/5 War),NYSE,Stock,2021-02-02,null,Active\n"+
                "AAC-WS,Ares Acquisition Corporation - Warrants (01/01/9999),NYSE,Stock,2021-03-25,null,Active\n"+
                "AACG,ATA Creativity Global,NASDAQ,Stock,2008-01-29,null,Active";

        String myFile = "src/main/resources/companySymbols.csv";

        String AAFile = "src/main/resources/AA_data.csv";

        clearFile ( myFile );

        ArrayList<String> arr = csvToArrayListWithLimit ( csvToString ( AAFile ),0 );

        //Clears blank first index
//        arr.trimToSize ();
//
//        arr.stream ().forEach ( s -> writer ( myFile,s ) );
//
//        System.out.println ( csvToString (myFile));

    }
}
