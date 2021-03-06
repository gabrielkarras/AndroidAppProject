package com.example.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class NetworkUltility {
    private final static String WEATHER_API_BASE = "https://api.darksky.net/forecast/2c7a7a40eeca1108d7de3964990cb72b/";
    private static String latLong = "";
    private static String lastLatLong = "";

    public static URL buildURLForWeather(final String cityName, final Context context) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                getLatLong(cityName, context);
            }
        });
        int timeKeeping = 0;

        if (latLong == null){
            latLong = "";
        }

        if(lastLatLong == null){
            lastLatLong = "";
        }

        while ((latLong == "" || latLong.toLowerCase().equals(lastLatLong.toLowerCase())) && timeKeeping <= 1500)
        {
            try {
                //set time in mili
                //TODO: might cause an issue with other async functions
                Thread.sleep(10);
                timeKeeping = timeKeeping +10;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Uri buildUri = Uri.parse(WEATHER_API_BASE).buildUpon().appendPath(latLong).appendQueryParameter("exclude","minutely,hourly,flags,alerts").build();
        lastLatLong = latLong;

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

     private static void getLatLong(String city, Context context)
     {
         if(Geocoder.isPresent()){
             try {
                 Geocoder gc = new Geocoder(context, Locale.getDefault());
                 List<Address> addresses= gc.getFromLocationName(city, 1); // get the found Address Objects

                 if (addresses.isEmpty())
                     //TODO add current device GPS coords instead of hardcoded
                     latLong =  "45.5036,-73.5527";
                 else
                     latLong = addresses.get(0).getLatitude() + "," + addresses.get(0).getLongitude();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }
}
