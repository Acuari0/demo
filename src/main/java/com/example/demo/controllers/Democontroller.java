package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class Democontroller {
    @GetMapping("/api/demo")
    public String helloMessage(){
        return "Hello word";
    }
    
}
