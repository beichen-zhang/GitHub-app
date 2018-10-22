package com.example.masan528.github;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
    private String url;
    public client(String url){
        this.url = url;
    }

    private static String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }

    public String getHTML(String urlQueryString) throws Exception {
        String json = null;
        try {
            URL url = new URL(urlQueryString);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(url.openStream())
            );
            String stringBuffer;
            String stringText = "";
            while ((stringBuffer = bufferedReader.readLine())!= null){
                stringText += stringBuffer;
            }
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public String get_result()throws Exception{
        return this.getHTML(this.url);
    }
}
