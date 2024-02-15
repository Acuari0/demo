package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.Site;

@Repository
public interface SiteRepo extends JpaRepository<Site,UUID> {

    List<Site> findByStatus(String status);
    int deleteByUrl(String url);
    
}
