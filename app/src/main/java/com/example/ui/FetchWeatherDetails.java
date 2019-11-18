package com.example.ui;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class FetchWeatherDetails extends AsyncTask<URL, Void, String> {

    private String TAG = "FetchWeatherDetails ";
    private String weatherSearchResults = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL weatherUrl = urls[0];

        try {
            weatherSearchResults = NetworkUltility.getResponseFromHttpUrl(weatherUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "doInBackground: weatherSearchResults: " + weatherSearchResults);
        return weatherSearchResults;
    }

    public WeatherForecastInformation parseJSON() {

        if(weatherSearchResults != null) {
            try {
                JSONObject rootObject = new JSONObject(weatherSearchResults).getJSONObject("daily");
                JSONArray results = rootObject.getJSONArray("data");

                WeatherForecastInformation weatherInfo = new WeatherForecastInformation();

                double MaxTemp = results.getJSONObject(0).getDouble("apparentTemperatureHigh");
                double MinTemp = results.getJSONObject(0).getDouble("apparentTemperatureLow");
                double DecimalMaxTemp = results.getJSONObject(0).getDouble("temperatureHigh");
                double DecimalMinTemp = results.getJSONObject(0).getDouble("temperatureLow");
                double Wind = results.getJSONObject(0).getDouble("windSpeed");
                double WindGust = results.getJSONObject(0).getDouble("windGust");
                String WeatherCondition = results.getJSONObject(0).getString("precipType");
                String WeatherConditionPhrase = results.getJSONObject(0).getString("summary");

                weatherInfo.setMaxtemp(MaxTemp);
                weatherInfo.setMintemp(MinTemp);
                weatherInfo.setDecimalMaxtemp(DecimalMaxTemp);
                weatherInfo.setDecimalMintemp(DecimalMinTemp);
                weatherInfo.setWindSpeed(Wind);
                weatherInfo.setWindGustSpeed(WindGust);
                weatherInfo.setWeatherCondition(WeatherCondition);
                weatherInfo.setWeatherConditionPhrase(WeatherConditionPhrase);
                return weatherInfo;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
