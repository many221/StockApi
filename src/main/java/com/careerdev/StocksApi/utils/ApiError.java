package com.careerdev.StocksApi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

public class ApiError {

    public static ResponseEntity<?> genericApiError (Exception e) {
        System.out.println(e.getMessage());
        System.out.println(e.getClass());
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    public static ResponseEntity<?> customApiError (String message, int status) {
        return ResponseEntity.status(status).body(message);
    }

    public static void throwErr(int status, String message) throws HttpClientErrorException {

        throw new HttpClientErrorException ( HttpStatus.valueOf ( status ), message );

    }

    public static boolean isStrNaN(String strNum) {
        if (strNum == null) {
            return true;
        }
        try {
            Long.parseLong ( strNum );
        } catch (NumberFormatException nfe) {
            return true;
        }
        return false;
    }

}