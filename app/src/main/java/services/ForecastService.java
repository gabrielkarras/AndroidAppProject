package services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class ForecastService extends IntentService {

    private final static String WEATHER_API_BASE = "https://api.darksky.net/forecast/2c7a7a40eeca1108d7de3964990cb72b/";
    private URL geocodedURL;
    private String rawForecastData;
    private String city;
    private JSONObject cleanForecast;
    private ResultReceiver receiver;

    public ForecastService(){
        super("Geocoder Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent == null) {
            return;
        }

        receiver = intent.getParcelableExtra("receiver");
        formGeocodedURL(intent);
        if (geocodedURL != null){
            fetchWeatherForecast();
            cleanWeatherData();
        }

        if (cleanForecast != null){
            try{
                cleanForecast.put("request_TimeMillis",System.currentTimeMillis());
                cleanForecast.put("request_City",city);
                deliverResultToReceiver(1,cleanForecast);

            } catch(JSONException exc){
                exc.printStackTrace();
            }

        } else {
            deliverResultToReceiver(0,null);
        }
    }

    private void deliverResultToReceiver(int resultCode, JSONObject result) {
        Bundle bundle = new Bundle();
        if (result != null){
            bundle.putString("JSONString", result.toString());
            receiver.send(resultCode, bundle);
        } else {
            receiver.send(resultCode,bundle);
        }
    }

    private void formGeocodedURL(Intent intent){

        Address foundGeoCoords = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        city = intent.getStringExtra("location");
        String geoLocation = null;

        //Fetch Geocode coords
        try {
            foundGeoCoords = geocoder.getFromLocationName(city, 1).get(0);
            city = foundGeoCoords.getFeatureName();
        } catch (IOException ioException) {
            Log.e("GEOCODER SERVICE","IO exception");
            ioException.printStackTrace();
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e("GEOCODER SERVICE","Illegal Argument");
            illegalArgumentException.printStackTrace();
        } catch (NullPointerException nullPointer) {
            Log.e("GEOCODER SERVICE","Geocoder Returned Null.");
            nullPointer.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        if(foundGeoCoords == null){
            //TODO add current device GPS coords instead of hardcoded
            geoLocation =  "45.5036,-73.5527";
            city = "Montreal";
        } else {
            geoLocation =  foundGeoCoords.getLatitude() + "," + foundGeoCoords.getLongitude();
        }

        //Build URL with geocode
        Uri buildUri = Uri.parse(WEATHER_API_BASE).buildUpon().appendPath(geoLocation).appendQueryParameter("exclude","currently,minutely,hourly,alerts").build();
        try {
            geocodedURL = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private void fetchWeatherForecast(){
        // Fetch Weather from the API
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) geocodedURL.openConnection();

            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner  = new Scanner(inputStream);

            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                rawForecastData = scanner.next();
            }
        } catch (IOException Io){

            Io.printStackTrace();

        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
        }
    }

    private void cleanWeatherData(){
        if(rawForecastData != null) {
            cleanForecast = new JSONObject();
            JSONArray cleanData = new JSONArray();
            String forecastUnits = "";
            String timezone = "";

            try {
                //Isolate only the necessary fields from the raw data
                JSONObject rootObject = new JSONObject(rawForecastData);
                JSONArray weeklyData = rootObject.getJSONObject("daily").getJSONArray("data");
                forecastUnits = rootObject.getJSONObject("flags").getString("units");
                timezone = rootObject.getString("timezone");

                for(int i =0; i<weeklyData.length(); i++){

                    JSONObject tempSrc = weeklyData.getJSONObject(i);
                    JSONObject tempDst = new JSONObject();

                    tempDst.put("time",tempSrc.getLong("time"));
                    tempDst.put("icon",tempSrc.getString("icon"));
                    tempDst.put("humidity",tempSrc.getDouble("humidity"));
                    tempDst.put("visibility",tempSrc.getDouble("visibility"));

                    tempDst.put("apparentTemperatureHigh",tempSrc.getDouble("apparentTemperatureHigh"));
                    tempDst.put("apparentTemperatureLow",tempSrc.getDouble("apparentTemperatureLow"));
                    tempDst.put("temperatureHigh",tempSrc.getDouble("temperatureHigh"));
                    tempDst.put("temperatureLow",tempSrc.getDouble("temperatureLow"));
                    tempDst.put("windSpeed",tempSrc.getDouble("windSpeed"));
                    tempDst.put("windGust",tempSrc.getDouble("windGust"));
                    if(tempSrc.has("precipType")) {
                        tempDst.put("precipType", tempSrc.getString("precipType"));
                        tempDst.put("precipProbability", tempSrc.getString("precipProbability"));
                    }
                    tempDst.put("summary",tempSrc.getString("summary"));


                    cleanData.put(tempDst);
                }

                cleanForecast.put("data",cleanData);
                cleanForecast.put("units",forecastUnits);
                cleanForecast.put("timezone",timezone);
            } catch (JSONException exc){
                exc.printStackTrace();
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }
}
