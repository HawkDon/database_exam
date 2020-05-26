package com.oliverloenning.redis;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    public static String cascadeString(String title) {
     return title.replace(" ", "%20");
    }

    public static String requestResource(String resource ) throws IOException {
        URL url = new URL(resource);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            content.append(inputLine);
        }
        br.close();

        return content.toString();
    }
}
