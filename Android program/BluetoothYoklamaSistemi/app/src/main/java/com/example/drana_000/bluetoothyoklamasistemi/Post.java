package com.example.drana_000.bluetoothyoklamasistemi;

/**
 * Created by Drana_000 on 17.5.2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Post {
    public static String gonder(String site , String json)
    {
        URL url;
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();

        try{
            url=new URL(site);
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            // out.write(json.toString());
            out.write(json);
            out.close();

            int HttpResult =connection.getResponseCode();

            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");

                }
                br.close();

                return sb.toString();

            }
            else
            {
                return "null";
            }

        }
        catch (MalformedURLException e)
        {

            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "null";
    }






}