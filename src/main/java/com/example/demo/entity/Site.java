package com.example.demo.entity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.UUID;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="site")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Site {
    @Id
    @GeneratedValue()
    private UUID id;

    @Column(nullable = false)
    private String status;

    private String url,content;


    public HashMap<String, String> getResponceCheck(){
        HashMap<String, String> map = new HashMap<>();
        if(getStatus()!=null)
            map.put("status", getStatus());
        return map;
    }

   

    public void searchWord(){
        String contenido = "";
        boolean isOk=true;
        try {
            URL urlObj = new URL(url);
            URLConnection connection = urlObj.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido += linea;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            isOk=false;
        }
    
        // Buscamos la palabra en el contenido
        if(contenido.indexOf(content)>-1){
            setStatus("rejected");
        }
        else{
            setStatus("accepted");
        }
        if (!isOk) {
            setStatus("rejected");
        }
    }

}
