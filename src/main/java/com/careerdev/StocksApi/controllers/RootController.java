package com.careerdev.StocksApi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

    public ResponseEntity<?> rootRoute(){

        return ResponseEntity.ok ("Root Route");
    }
}
