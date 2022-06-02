package com.careerdev.StocksApi.utils;

import java.util.ArrayList;

public class Csv {

    // Takes A String of CSV data then parses it then returns it as an array list


    public static ArrayList<String> csvToArrayListByColumn (String csv, int column)  {

        ArrayList<String> strArr = new ArrayList<> ();

        csv.lines ().skip ( 1 ).forEach ( s -> strArr.add ( s.split ( "," )[column] )  );

        return strArr;


//        ArrayList<String[]> arr = new ArrayList<> ();
//        csv.lines ().skip ( 1 ).forEach ( s -> arr.add ( s.split ( "," ) )  );
       // arr.stream ().forEach ( s -> System.out.println ( s[0]) );
//        List<String> tickList = null;
//        Iterator<String[]> i = arr.iterator ();
//        while(i.hasNext ()){
//
//            tickList.add ( i.next ()[0] );
//
//       }
       // tickList.stream ().forEach ( System.out::println );


    }

    public static ArrayList<String> csvToArrayListByColumn (String csv,int column, long limit) {

        ArrayList<String> strArr = new ArrayList<> ();

        csv.lines ().skip ( 1 ).limit ( limit ).forEach ( s -> strArr.add ( s.split ( "," )[column] ) );

        return strArr;
    }

    public static ArrayList<String[]> csvToArrayList (String csv) {

        ArrayList<String[]> strArr = new ArrayList<> ();

        csv.lines ().skip ( 1 ).forEach ( s -> strArr.add ( s.split ( "," ) ) );

        return strArr;
    }

    public static ArrayList<String[]> csvToArrayList (String csv, long limit) {

        ArrayList<String[]> strArr = new ArrayList<> ();

        csv.lines ().skip ( 1 ).limit ( limit ).forEach ( s -> strArr.add ( s.split ( "," ) ) );

        return strArr;
    }

//
//    public static void main(String[] args) {
//
//        String test = "symbol,name,exchange,assetType,ipoDate,delistingDate,status\n" +
//                "A,Agilent Technologies Inc,NYSE,Stock,1999-11-18,null,Active\n" +
//                "AA,Alcoa Corp,NYSE,Stock,2016-10-18,null,Active\n" +
//                "AAA,AAF FIRST PRIORITY CLO BOND ETF ,NYSE ARCA,ETF,2020-09-09,null,Active\n" +
//                "AAAU,Goldman Sachs Physical Gold ETF,BATS,ETF,2018-08-15,null,Active\n" +
//                "AAC,Ares Acquisition Corporation - Class A,NYSE,Stock,2021-03-25,null,Active";
//
//        System.out.println (csvToArrayList ( test ));
//
//    }
}
