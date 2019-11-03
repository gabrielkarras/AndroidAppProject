package com.example.ui;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUltility {
    private final static String WEATHER_API_BASE = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/56186";
    private final static String API_KEY = "xWh7854v9xS9dAZZkT5LdZ6pLTMCrLlk";
    private final static String PARAM_API_KEY = "apikey";
    private final static String PARAM_DETAILS = "details";

    public static URL buildURLForWeather()
    {
        Uri buildUri = Uri.parse(WEATHER_API_BASE).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_DETAILS, "true").build();

        URL url = null;
        try
        {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i("Network", "buildURLForWeather URL: " + url);
        return url;
    }

     public static String getResponseFromHttpUrl (URL url) throws IOException
     {
         HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

         try
         {
             InputStream inputStream = urlConnection.getInputStream();
             Scanner scanner  = new Scanner(inputStream);

             scanner.useDelimiter("\\A");

             boolean hasInput = scanner.hasNext();
             if(hasInput)
             {
                 return scanner.next();
             }
             else
             {
                 return null;
             }
         }
         finally {
             urlConnection.disconnect();
         }
     }
}
