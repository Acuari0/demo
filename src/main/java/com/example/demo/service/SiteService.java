package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Site;
import com.example.demo.repository.SiteRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SiteService {
    @Autowired
    private SiteRepo siteRepo;

    public List<Site> getAllAprovedSites(){
        return siteRepo.findByStatus("accepted");
    }

    public int removeUrl(String url){
        return siteRepo.deleteByUrl(url);
    }
    
}
