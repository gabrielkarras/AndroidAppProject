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

//    @Override
//    protected void onPostExecute(String weatherSearchResults) {
//        if(weatherSearchResults != null && !weatherSearchResults.equals(""))
//        {
//            Log.i(TAG, "onPostExecute: weatherSearchResults: " + weatherSearchResults);
//        }
//        super.onPostExecute(weatherSearchResults);
//    }

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
//                double RainTotal = results.getJSONObject(0).getJSONObject("Day")
//                        .getJSONObject("Rain").getDouble("Value");
//                double SnowTotal = results.getJSONObject(0).getJSONObject("Day")
//                        .getJSONObject("Snow").getDouble("Value");
                ////TODO: next to change how to process this information
                String WeatherCondition = results.getJSONObject(0).getString("precipType");
                String WeatherConditionPhrase = results.getJSONObject(0).getString("summary");
//                int RainProbability = results.getJSONObject(0).getJSONObject("Day")
//                        .getInt("RainProbability");
//                int SnowProbability = results.getJSONObject(0).getJSONObject("Day")
//                        .getInt("SnowProbability");

                weatherInfo.setMaxtemp(MaxTemp);
                weatherInfo.setMintemp(MinTemp);
                weatherInfo.setDecimalMaxtemp(DecimalMaxTemp);
                weatherInfo.setDecimalMintemp(DecimalMinTemp);
                weatherInfo.setWindSpeed(Wind);
                weatherInfo.setWindGustSpeed(WindGust);
//                weatherInfo.setRainTotal(RainTotal);
//                weatherInfo.setSnowTotal(SnowTotal);
//                weatherInfo.setGonnaRain(RainProbability);
//                weatherInfo.setGonnaSnow(SnowProbability);
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
