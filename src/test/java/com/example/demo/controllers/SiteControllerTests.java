package com.example.demo.controllers;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.demo.entity.Site;
import com.example.demo.repository.SiteRepo;
import com.example.demo.service.SiteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;



@AutoConfigureMockMvc
@WebMvcTest(controllers = SiteController.class)
public class SiteControllerTests {
    @Autowired
    private MockMvc mockMvc;
    

    @MockBean
    private SiteService siteService;

    @MockBean
     private SiteRepo siteRepo;
    
    private  Site site;


    @SuppressWarnings("null")
    @Test
    @DisplayName("Respuesta de rechazo de URL")
    public void rejectedCheckUrl() throws Exception{
       site= new Site();
       
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        
        List<String> list_url=new ArrayList<>(), 
        list_content= new ArrayList<>();

        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Zlatan_Ibrahimovi%C4%87");
        list_content.add("ronaldo");
        list_content.add("mls");
        list_content.add("suecia");

        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            json=ow.writeValueAsString(site);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/content/check")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("status", Matchers.is("rejected")));

        }
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Respuesta de Aceptacion de URL")
    public void acceptCheckUrl() throws Exception{
       site= new Site();
       
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        
        List<String> list_url=new ArrayList<>(), 
        list_content= new ArrayList<>();

        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Zlatan_Ibrahimovi%C4%87");
        list_content.add("Venez");
        list_content.add("Merida");
        list_content.add("ULA");

        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            json=ow.writeValueAsString(site);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/content/check")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("status", Matchers.is("accepted")));

        }
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Guardado de varios en en authorized/indexed")
    public void acceptInAuthorizedIndexedList() throws Exception{
       site= new Site();
       
      
        
        List<String> list_url=new ArrayList<>(), 
       
        list_content= new ArrayList<>();
        List<Site> sites=new ArrayList<>();

        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Zlatan_Ibrahimovi%C4%87");
        list_content.add("Venez");
        list_content.add("Merida");
        list_content.add("ULA");

        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            site.setId(UUID.randomUUID());
            site.setStatus("accepted");
            sites.add(new Site(UUID.randomUUID(),  site.getStatus(), site.getUrl(), site.getContent()));
            
        }
        
        Mockito.when(siteService.getAllAprovedSites()).thenReturn(sites);
        mockMvc.perform(MockMvcRequestBuilders.get("/authorized/indexed"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].url", Matchers.is(sites.get(0).getUrl())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].url", Matchers.is(sites.get(1).getUrl())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[2].url", Matchers.is(sites.get(2).getUrl())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", Matchers.is(sites.get(0).getContent())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].content", Matchers.is(sites.get(1).getContent())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[2].content", Matchers.is(sites.get(2).getContent())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(sites.get(0).getStatus())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].status", Matchers.is(sites.get(1).getStatus())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[2].status", Matchers.is(sites.get(2).getStatus())));

    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Guardado un mismo en authorized/indexed")
    public void acceptOnlyOneInAuthorizedIndexedList() throws Exception{
       site= new Site();
        
        List<String> list_url=new ArrayList<>(), 
        list_content= new ArrayList<>(),
        list_only= new ArrayList<>();
        List<Site> sites=new ArrayList<>();
        
        list_only.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_content.add("Venez");
        list_content.add("Merida");
        
        

        for(int i=0;i<list_url.size();i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            site.setId(UUID.randomUUID());
            site.setStatus("accepted");
            sites.add(new Site(UUID.randomUUID(),  site.getStatus(), site.getUrl(), site.getContent()));
        }
        Mockito.when(siteService.getAllAprovedSites()).thenReturn(sites);
        mockMvc.perform(MockMvcRequestBuilders.get("/authorized/indexed"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].url", Matchers.is(sites.get(0).getUrl())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", Matchers.is(sites.get(0).getContent())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(sites.get(0).getStatus())));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Lista vacia authorized/indexed")
    public void emptyAuthorizedIndexedList() throws Exception{
       site= new Site();
        
        List<Site> sites=new ArrayList<>();
        
        Mockito.when(siteService.getAllAprovedSites()).thenReturn(sites);
        mockMvc.perform(MockMvcRequestBuilders.get("/authorized/indexed"))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(sites)));
    }


    @SuppressWarnings("null")
    @Test
    @DisplayName("Obtener authorized sites /api/content con elementos")
    public void withElementsGetAuthorizedSites() throws Exception{
       site= new Site();
       
        
        List<String> list_url=new ArrayList<>(), 
       
        list_content= new ArrayList<>();
        List<Site> sites=new ArrayList<>();

        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Zlatan_Ibrahimovi%C4%87");
        list_content.add("Venez");
        list_content.add("Merida");
        list_content.add("ULA");

        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            site.setId(UUID.randomUUID());
            site.setStatus("accepted");
            sites.add(new Site(UUID.randomUUID(),  site.getStatus(), site.getUrl(), site.getContent()));
           
        }
        
        Mockito.when(siteService.getAllAprovedSites()).thenReturn(sites);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/content"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.
        jsonPath("$", Matchers.containsInAnyOrder(sites.stream().map((e)->e.getUrl()).toArray())));

    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Obtener authorized sites con elementos repetidos /api/content con elementos")
    public void withReapetedElementsGetAuthorizedSites() throws Exception{
       site= new Site();
       
        
        List<String> list_url=new ArrayList<>(), 
       
        list_content= new ArrayList<>();
        List<Site> sites=new ArrayList<>(),sites2= new ArrayList<>();

        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        
        list_content.add("Venez");
        list_content.add("Merida");
        list_content.add("Merida");

       
        
        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            site.setId(UUID.randomUUID());
            site.setStatus("accepted");
            sites.add(new Site(UUID.randomUUID(),  site.getStatus(), site.getUrl(), site.getContent()));
            sites2.add(new Site(UUID.randomUUID(),  site.getStatus(), site.getUrl(), site.getContent()));
        }
       
        Mockito.when(siteService.getAllAprovedSites()).thenReturn(sites);
       
        mockMvc.perform(MockMvcRequestBuilders.get("/api/content"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.
        jsonPath("$", Matchers.containsInAnyOrder(sites2.stream().map((e)->e.getUrl()).toArray())));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Obtener authorized vacio /api/content con elementos")
    public void withEmptyListGetAuthorizedSites() throws Exception{
       site= new Site();
        List<Site> sites=new ArrayList<>();

        Mockito.when(siteService.getAllAprovedSites()).thenReturn(sites);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/content"))
        .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Obtener authorized sites /api/content con elementos")
    public void OKDeleteAuthorizedSite() throws Exception{
       site= new Site();
       
        
        List<String> list_url=new ArrayList<>(), 
       
        list_content= new ArrayList<>();
        List<Site> sites=new ArrayList<>();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        List<String> json =new ArrayList<>();

        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Zlatan_Ibrahimovi%C4%87");
        list_content.add("Venez");
        list_content.add("Merida");
        list_content.add("ULA");

        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            site.setId(UUID.randomUUID());
            site.setStatus("accepted");
            sites.add(new Site(UUID.randomUUID(),  site.getStatus(), site.getUrl(), site.getContent()));
            json.add(ow.writeValueAsString(site));
        }
        
        if(sites.stream().anyMatch((e)->e.getUrl().equals("https://es.wikipedia.org/wiki/Cristiano_Ronaldo"))){
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(1);
            
        }
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/content")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json.get(0))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Obtener authorized sites /api/content con elementos")
    public void OKDoubleDeleteAuthorizedSite() throws Exception{
       site= new Site();
       
        
        List<String> list_url=new ArrayList<>(), 
       
        list_content= new ArrayList<>();
        List<Site> sites=new ArrayList<>();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        List<String> json =new ArrayList<>();

        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Zlatan_Ibrahimovi%C4%87");
        list_content.add("Venez");
        list_content.add("Merida");
        list_content.add("ULA");

        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            site.setId(UUID.randomUUID());
            site.setStatus("accepted");
            sites.add(new Site(UUID.randomUUID(),  site.getStatus(), site.getUrl(), site.getContent()));
            json.add(ow.writeValueAsString(site));
        }
        
        if(sites.stream().anyMatch((e)->e.getUrl().equals("https://es.wikipedia.org/wiki/Cristiano_Ronaldo"))){
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(1);
        }
        else{
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(0);
        }
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/content")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json.get(0))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());

        sites=sites.stream().filter((e)->!e.getUrl().equals("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).toList();

        if(sites.stream().anyMatch((e)->e.getUrl().equals("https://es.wikipedia.org/wiki/Cristiano_Ronaldo"))){
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(1);
            
        }
        else{
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(0);
        }

       
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/content")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json.get(0))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Obtener authorized sites /api/content con elementos")
    public void NoDeleteAuthorizedSite() throws Exception{
       site= new Site();
       
        
        List<String> list_url=new ArrayList<>(), 
       
        list_content= new ArrayList<>();
        List<Site> sites=new ArrayList<>();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        List<String> json =new ArrayList<>();

        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Zlatan_Ibrahimovi%C4%87");
        list_content.add("Venez");
        list_content.add("Merida");
        list_content.add("ULA");

        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            site.setId(UUID.randomUUID());
            site.setStatus("accepted");
            sites.add(new Site(UUID.randomUUID(),  site.getStatus(), site.getUrl(), site.getContent()));
            json.add(ow.writeValueAsString(site));
        }
        
        if(sites.stream().anyMatch((e)->e.getUrl().equals("https://es.wikipedia.org/wiki/Cristiano_Ronaldo"))){
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(1);
        }
        else{
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(0);
        }
        
       
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/content")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json.get(0))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(204));
    }


    @SuppressWarnings("null")
    @Test
    @DisplayName("Respuesta de rechazo de URL")
    public void testAll() throws Exception{
       site= new Site();
       List<Site> sites=new ArrayList<>();
       
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        
        List<String> list_url=new ArrayList<>(), 
        list_content= new ArrayList<>();
        List<String> jsonList =new ArrayList<>();

        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Zlatan_Ibrahimovi%C4%87");
        list_content.add("ronaldo");
        list_content.add("mls");
        list_content.add("suecia");

        Mockito.when(siteService.getAllAprovedSites()).thenReturn(sites);
       
        mockMvc.perform(MockMvcRequestBuilders.get("/api/content"))
        .andExpect(MockMvcResultMatchers.status().is(204));

        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            json=ow.writeValueAsString(site);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/content/check")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("status", Matchers.is("rejected")));

        }

        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_content.add("Veloa");
        list_content.add("Merwwwida");
        list_content.add("canor");

        for(int i=3;i<6;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            site.setId(UUID.randomUUID());
            site.setStatus("accepted");
            json=ow.writeValueAsString(site);
            if(!sites.stream().anyMatch((e)->!e.getUrl().equals(site.getUrl()))){
              
                sites.add(new Site(UUID.randomUUID(),  site.getStatus(), site.getUrl(), site.getContent()));
                jsonList.add(ow.writeValueAsString(site));
            }
                
           
            mockMvc.perform(MockMvcRequestBuilders.post("/api/content/check")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("status", Matchers.is("accepted")));
        }
     
        Mockito.when(siteService.getAllAprovedSites()).thenReturn(sites);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/content"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.
        jsonPath("$", Matchers.containsInAnyOrder(sites.stream().map((e)->e.getUrl()).toArray())));

        if(sites.stream().anyMatch((e)->e.getUrl().equals("https://es.wikipedia.org/wiki/Cristiano_Ronaldo"))){
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(1);
        }
        else{
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(0);
        }
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/content")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonList.get(0))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());

        sites=sites.stream().filter((e)->!e.getUrl().equals("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).toList();

        if(sites.stream().anyMatch((e)->e.getUrl().equals("https://es.wikipedia.org/wiki/Cristiano_Ronaldo"))){
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(1);
            
        }
        else{
            Mockito.when(siteService.removeUrl("https://es.wikipedia.org/wiki/Cristiano_Ronaldo")).thenReturn(0);
        }

       
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/content")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonList.get(0))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(204));
    }
    
}
