package com.example.demo.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Site;
import com.example.demo.repository.SiteRepo;

@ExtendWith(MockitoExtension.class)
public class SiteServiceTests {
    @Mock
    private SiteRepo siteRepo;

    @BeforeEach
    public List<Site> getAllAprovedSites(){
        return siteRepo.findByStatus("accepted");
    }

    @BeforeEach
    public int removeUrl(String url){
        return siteRepo.deleteByUrl(url);
    }

}
