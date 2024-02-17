package com.example.demo.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

public class SiteTest {
    private Site site;


    @Test
    @DisplayName("Respuesta de rechazo de URL")
    public void RejectedGetResponseCheck(){
        site= new Site();
        site.setStatus("rejected");
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "rejected");
        Assertions.assertEquals(map, site.getResponceCheck());
    }
    @Test
    @DisplayName("Respuesta de aceptacion de URL")
    public void AcceptedGetResponseCheck(){
        site= new Site();
        site.setStatus("accepted");
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "accepted");
        Assertions.assertEquals(map, site.getResponceCheck());
    }

    @Test
    @DisplayName("Respuesta de aceptacion de URL en caso de que no se aceptara ni rechazara")
    public void EmptyGetResponseCheck(){
        site= new Site();
       
        HashMap<String, String> map = new HashMap<>();
        Assertions.assertEquals(map, site.getResponceCheck());
    }

    @Test
    @DisplayName("Se encontro palabra en el sitio web")
    public void foundSearchWord(){
        site= new Site();
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
            site.searchWord();
            Assertions.assertEquals("rejected", site.getStatus());
        }
    }

    @Test
    @DisplayName("No Se encontro palabra en el sitio web")
    public void notFoundSearchWord(){
        site= new Site();
        List<String> list_url=new ArrayList<>(), 
        list_content= new ArrayList<>();
        list_url.add("https://es.wikipedia.org/wiki/Cristiano_Ronaldo");
        list_url.add("https://es.wikipedia.org/wiki/Lionel_Messi");
        list_url.add("https://es.wikipedia.org/wiki/Zlatan_Ibrahimovi%C4%87");
        list_content.add("Venezuela");
        list_content.add("Merida");
        list_content.add("Ula");
        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            site.searchWord();
            Assertions.assertEquals("accepted", site.getStatus());
        }
    }

    @Test
    @DisplayName("No Se encontro palabra en el sitio web")
    public void incorrectPageSearchWord(){
        site= new Site();
        List<String> list_url=new ArrayList<>(), 
        list_content= new ArrayList<>();
        list_url.add("https://es.wikiped");
        list_url.add("https://es");
        list_url.add("https:");
        list_content.add("Venezuela");
        list_content.add("Merida");
        list_content.add("Ula");
        for(int i=0;i<3;i++){
            site.setUrl(list_url.get(i));
            site.setContent(list_content.get(i));
            site.searchWord();
            Assertions.assertEquals("rejected", site.getStatus());
        }
    }
}
