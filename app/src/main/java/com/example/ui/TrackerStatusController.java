package com.example.ui;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.appcompat.app.AppCompatActivity;

public class TrackerStatusController {
    private Context context;
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private AppCompatActivity caller_activity;
    private WeatherController mWeatherController;

    public enum UserTrackerStatus
    {
        INVALID, FORGOT_TRACKER, LEAVING_WITH_TRACKER;
    }

    public TrackerStatusController(Context context, AppCompatActivity caller, WeatherController WxController)
    {
        this.context = context;
        caller_activity = caller;
        mWeatherController = WxController;
        setUpWifi();
    }

    private void setUpWifi()
    {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    public UserTrackerStatus checkTrackerActionStatus()
    {
        int numberOfLevels = 10;
        int ThresholdWifiLevel = 3;
        int WifiLevel = WifiManager.calculateSignalLevel(mWifiInfo.getRssi(), numberOfLevels);

        if (mWeatherController.isGonnaRain())
        {
            if (WifiLevel > ThresholdWifiLevel)
                return UserTrackerStatus.INVALID;
            else
            {
                //TODO: need to request this information from BT device
                boolean TrackerIsMoving = true;
                if (!TrackerIsMoving)
                    return UserTrackerStatus.FORGOT_TRACKER;
                else
                    return UserTrackerStatus.LEAVING_WITH_TRACKER;
            }
        }
        else
            return UserTrackerStatus.INVALID;
    }

    ///////////////////////////////////////////THREADING WORK////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////
}
