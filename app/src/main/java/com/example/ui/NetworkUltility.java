package com.example.ui;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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

    public static URL buildURLForWeather(String cityName, Context context)
    {
        String latLong = getLatLong(cityName, context);
        Uri buildUri = Uri.parse(WEATHER_API_BASE).buildUpon()
                .appendPath(latLong).build();

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

     private static String getLatLong(String city, Context context)
     {
         if(Geocoder.isPresent()){
             try {
                 Geocoder gc = new Geocoder(context, Locale.getDefault());
                 List<Address> addresses= gc.getFromLocationName(city, 1); // get the found Address Objects

                 if (addresses.isEmpty())
                     return null;
                 else
                    return addresses.get(0).getLatitude() + "," + addresses.get(0).getLongitude();
             } catch (IOException e) {
                 // handle the exception
                 e.printStackTrace();
             }
         }
         return null;
     }
}
