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
    private final static String WEATHER_API_BASE = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/";
    private final static String API_KEY = "xWh7854v9xS9dAZZkT5LdZ6pLTMCrLlk";
    private final static String PARAM_API_KEY = "apikey";
    private final static String PARAM_DETAILS = "details";
    private final static String LOCATION_API_BASE = "http://dataservice.accuweather.com/locations/v1/cities/search";
    private final static String LOCATION_PARAM = "q";
    private static String LOCATION = "56186"; //set default location to Montreal

    public static void setLocation(String location)
    {
        LOCATION = location;
    }

    public static URL buildURLForWeather()
    {
        Uri buildUri = Uri.parse(WEATHER_API_BASE).buildUpon()
                .appendPath(LOCATION)
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

    public static URL buildURLForLocation(String cityName)
    {
        Uri buildUri = Uri.parse(LOCATION_API_BASE).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(LOCATION_PARAM, cityName).build();

        URL url = null;
        try
        {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i("Network", "buildURLForLocation URL: " + url);
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
