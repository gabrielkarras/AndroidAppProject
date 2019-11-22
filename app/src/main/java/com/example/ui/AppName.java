package com.example.ui;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import services.ServiceController;

public class AppName extends Application {

    /**  THIS CLASS NECESSARY TO DEFINE NOTIFICATION CHANNELS FOR OREO+ **/

    public static final String MANDATORY_NOTIFICATION_CHANNEL = "Background tracking started";
    public static final String MANDATORY_NOTIFICATION_CHANNEL__ID = "MANDATORY_CHANNEL_1";

    public static final String SUGGESTION_NOTIFICATION_CHANNEL = "Daily suggestions";
    public static final String SUGGESTION_NOTIFICATION_CHANNEL__ID = "SUGGESTION_CHANNEL_1";

    public static final String TRACKER_NOTIFICATION_CHANNEL = "Lost and Found tags";
    public static final String TRACKER_NOTIFICATION_CHANNEL__ID = "TRACKER_CHANNEL_1";
    
    protected static ArrayList<TagObj> registeredTags;
    protected static ServiceController serviceController;
    protected static boolean notificationsEnabled;

    protected static SharedPreferences preferences;
    protected static SharedPreferences.Editor preferencesEditor;

    @Override
    public void onCreate() {
        super.onCreate();
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        createNotificationChannel();

        preferences = getSharedPreferences("forecasterAppTrackerList",Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();

        //Load tags from memory
        loadTrackedTagList();

        serviceController = new ServiceController(this,registeredTags);

        //If notifications enabled schedule next notification alarm
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()){
            notificationsEnabled = true;
        } else {
            notificationsEnabled = false;
        }
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel suggestion_channel = new NotificationChannel(SUGGESTION_NOTIFICATION_CHANNEL__ID, SUGGESTION_NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_LOW);
            suggestion_channel.setDescription("Suggestion notification channel.");

            NotificationChannel mandatory_channel = new NotificationChannel(MANDATORY_NOTIFICATION_CHANNEL__ID, MANDATORY_NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
            mandatory_channel.setDescription("Mandatory notification channel.");

            NotificationChannel tracker_channel = new NotificationChannel(TRACKER_NOTIFICATION_CHANNEL__ID, TRACKER_NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
            tracker_channel.setDescription("Tracker notification channel.");

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(suggestion_channel);
            notificationManager.createNotificationChannel(mandatory_channel);
            notificationManager.createNotificationChannel(tracker_channel);
        }
    }

    protected static void invokeTrackingService(){
        serviceController.startService();
    }

    private void loadTrackedTagList(){
        registeredTags = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(preferences.getString("JSONTagArray",""));
            for(int i =0; i<jsonArray.length();i++){
                JSONObject temp = jsonArray.getJSONObject(i);
                registeredTags.add(new TagObj(temp.getString("name"),temp.getString("address")));
            }
        }catch(Exception e){
            Log.e("JSONEXCEPTION","JSON CONVERSION LOADING EXCEPTION");
            e.printStackTrace();
        }

    };

    protected static void saveTrackedTagsList(){
        JSONArray jarray = new JSONArray();
        for(int i=0;i<registeredTags.size();i++){
            JSONObject temp = registeredTags.get(i).getJSONObject();
            if(temp != null){
                jarray.put(temp);
            }
        }

        preferencesEditor.putString("JSONTagArray",jarray.toString());
        preferencesEditor.commit();
    }


    @Override
    public void onTerminate() {
        saveTrackedTagsList();
        super.onTerminate();
    }
}
