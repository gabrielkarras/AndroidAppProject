package com.example.ui;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

import services.ServiceController;

public class AppName extends Application {

    /**  THIS CLASS NECESSARY TO DEFINE NOTIFICATION CHANNELS FOR OREO+ **/

    public static final String MANDATORY_NOTIFICATION_CHANNEL = "MANDATORY_CHANNEL";
    public static final String MANDATORY_NOTIFICATION_CHANNEL__ID = "MANDATORY_CHANNEL_1";

    public static final String SUGGESTION_NOTIFICATION_CHANNEL = "SUGGESTION_CHANNEL";
    public static final String SUGGESTION_NOTIFICATION_CHANNEL__ID = "SUGGESTION_CHANNEL_1";

    public static final String TRACKER_NOTIFICATION_CHANNEL = "TRACKER_CHANNEL";
    public static final String TRACKER_NOTIFICATION_CHANNEL__ID = "TRACKER_CHANNEL_1";
    
    protected static ArrayList<TagObj> registeredTags;
    protected static ServiceController serviceController;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        createNotificationChannel();

        //Load tags from memory
        loadTrackedTagList();

        serviceController = new ServiceController(this,registeredTags);

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
        //TODO load from Internal Storage
        registeredTags = new ArrayList<>();
        registeredTags.add(new TagObj("HOPE","ADD:ADA:DD"));
        registeredTags.add(new TagObj("POPA","DDD:PPP:QQ"));
    };

    protected void saveTrackedTagsList(){
        //TODO save to Internal Storage
    }

    @Override
    public void onTerminate() {
        saveTrackedTagsList();
        super.onTerminate();
    }
}
