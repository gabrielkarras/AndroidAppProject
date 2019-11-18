package services;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.ui.TagObj;

import java.util.ArrayList;

public class ServiceController {

    private Context context;
    private ArrayList<TagObj> trackingTagList;
    private boolean serviceStarted;


    public ServiceController(Context context,ArrayList<TagObj> trackingTagList){
        this.context = context;
        this.trackingTagList = trackingTagList;
    }

    public void updateTrackingTagList(ArrayList<TagObj> newList){

        if (serviceStarted) {
            sendTrackingServiceUpdateBroadcast(newList);
        }

        trackingTagList  = newList;
    }

    public void updateAndRun(ArrayList<TagObj> newList){
        updateTrackingTagList(newList);
        startService();
    }

    private void sendTrackingServiceUpdateBroadcast(ArrayList<TagObj> tagList){
        Intent intent = new Intent();
        intent.setAction("UPDATE_TRACKER_SERVICE");
        intent.putParcelableArrayListExtra("targetArrayList",tagList);
        context.sendBroadcast(intent);
    }


    public void startService(){
        if(!serviceStarted) {
            serviceStarted = true;

            Intent serviceIntent = new Intent(context, BLE_Service.class);
            if (trackingTagList != null && trackingTagList.size() != 0) {
                serviceIntent.putParcelableArrayListExtra("targetArrayList", trackingTagList);
            }

            //Handles version checks
            ContextCompat.startForegroundService(context, serviceIntent);
        }

    }

    public void stopService(){
        if(serviceStarted){
            serviceStarted = false;
            Intent serviceIntent = new Intent(context,BLE_Service.class);
            context.stopService(serviceIntent);
        }
    }

}
