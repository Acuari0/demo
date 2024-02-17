package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Site;
import com.example.demo.repository.SiteRepo;
import com.example.demo.service.SiteService;

import lombok.AllArgsConstructor;


import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;








@RestController
@AllArgsConstructor
public class SiteController {
    private final SiteRepo siteRepo;
    private final SiteService siteService;

    @PostMapping("/api/content/check")
    private ResponseEntity<?> checkUrl(@RequestBody Site site){
        site.searchWord();
        List<Site> sites=getAuthorizedIndexedList();
    
        if((site.getStatus()=="accepted"&& 
        !sites.stream()
        .anyMatch((e)->e.getUrl().trim().equals(site.getUrl().trim())))){
            siteRepo.save(site);
        }
      
        return new ResponseEntity<HashMap<String, String>>(site.getResponceCheck(), HttpStatus.OK);
    }

    @GetMapping("/authorized/indexed")
    private List<Site> getAuthorizedIndexedList() {
        return siteService.getAllAprovedSites();   
    }
    

    @GetMapping("/api/content")
    private ResponseEntity<?> getAuthorizedSites() {
        List<Site> sites=getAuthorizedIndexedList();
        
        if(sites.isEmpty())
            return new ResponseEntity<>(HttpStatus.valueOf(204));
        return ResponseEntity.ok(getAuthorizedIndexedList().stream().map((e)->e.getUrl()));
    }

    @DeleteMapping("/api/content")
    private ResponseEntity<?> removeAuthorizedSite(@RequestBody Site site) {
        int sucess=siteService.removeUrl(site.getUrl());
       
        if(sucess>=1)
            return new ResponseEntity<>(HttpStatus.valueOf(200));
        return new ResponseEntity<>(HttpStatus.valueOf(204));
    }   
}
