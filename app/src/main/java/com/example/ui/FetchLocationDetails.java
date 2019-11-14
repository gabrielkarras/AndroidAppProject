package com.example.ui;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class FetchLocationDetails extends AsyncTask<URL, Void, String> {

    private String TAG = "FetchWeatherDetails ";
    private String locationSearchResult = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL locationUrl = urls[0];

        try {
            locationSearchResult = NetworkUltility.getResponseFromHttpUrl(locationUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "doInBackground: LocationSearchResults: " + locationSearchResult);
        return locationSearchResult;
    }

    public boolean parseLocationFromJSON() {
        if(locationSearchResult != null) {
            try {
                JSONArray rootObject = new JSONArray(locationSearchResult);

                String locationCode = rootObject.getJSONObject(0).getString("Key");
                if (locationCode != null)
                {
                    NetworkUltility.setLocation(locationCode);
                    return true;
                }
                else
                    return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        else
            return false;
    }
}

