package services;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.ui.R;
import com.example.ui.TagObj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import BluetoothScanner.BLEscanner;

import static com.example.ui.AppName.MANDATORY_NOTIFICATION_CHANNEL__ID;
import static com.example.ui.AppName.TRACKER_NOTIFICATION_CHANNEL__ID;

public class BLE_Service extends Service{

    BLEscanner temp;
    Uri alarmSound;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            targetList = intent.getParcelableArrayListExtra("targetArrayList");
            //TODO when get a new list check if the lost trackers in it, if not remove them.
            //TODO WHAT THE HELL IMA DO WHEN MULTIPLE TAGS MISSING?
        }
    };


    private ArrayList<TagObj> targetList; //Supposed to be Tag list
    private ArrayList<TagObj> lostList; //Supposed to be Tag list


    @Override
    public void onCreate() {
        super.onCreate();

        /* If the current version is Oreo +, need to show a mandatory notification so the process
         wont get killed.*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMandatoryNotification();
        }

        registerReceiver(receiver, new IntentFilter("UPDATE_TRACKER_SERVICE"));
    }

    public void startScan(ArrayList<TagObj> registeredTags) {

        //TODO - APP DIES IF GIVEN NULL
        Log.e("DDDD", "STARTED !");
        List<String> tagAdressList = new ArrayList<>();
        for (TagObj tag: registeredTags) {
            tagAdressList.add(tag.getTagAddress());
        }
        temp.startScan(tagAdressList);
        //3C:71:BF:F1:E4:76
    }

    public void stopScan() {
        Log.e("DDDD", "STOPPED !");
        temp.stopScan();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        targetList = intent.getParcelableArrayListExtra("targetArrayList");

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported!", Toast.LENGTH_SHORT).show();
            //QUIT THE APP
        }

        temp = new BLEscanner(this);
        try {
            Thread.sleep(500);
        }catch (java.lang.InterruptedException e){
            Log.d("interupted","sleep");
        }
        startScan(targetList);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("update-UI-gatt"));
        //TODO scan for BLE devices
        // IF device in lost list, add it to target list.
        // IF device not found for more than N scans, put it into lost list from targetList.
        // INVOKE startLostNotification() or startFoundNotification()

        return START_STICKY;
    }

    public void startMandatoryNotification(){
        //TODO It is overriden by newer notifications, make timer and replace text accordingly. the service will stay live regardless.
        //TODO change application notification icons

        Notification notification = new NotificationCompat.Builder(this,MANDATORY_NOTIFICATION_CHANNEL__ID)
                .setContentTitle("CrazyForecaster Is Looking for Umbrellas!")
                .setContentText("Your device will track the tags while the app is running in background.")
                .setSmallIcon(R.drawable.ic_android_white_24dp)
                .build();
        startForeground(1,notification);

    }

    public void startLostNotification(String tagName){

        //TODO on-click start browser with GPS coordinates.

        // Those 2 allow to transition to the view on notification click!
        // Intent  notificationIntent = new Intent(this,MainActivity.class);
        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this,TRACKER_NOTIFICATION_CHANNEL__ID)
                .setContentTitle(tagName+" is lost!")
                .setContentText("The connection to your "+tagName+" tracker was lost.")
                .setSmallIcon(R.drawable.ic_android_white_24dp)
                .build();
        startForeground(2,notification);
    }

    public void startFoundNotification(String tagName){

        Notification notification = new NotificationCompat.Builder(this,TRACKER_NOTIFICATION_CHANNEL__ID)
                .setContentTitle(tagName +" is nearby!")
                .setContentText("The "+tagName+" tracker is near.")
                .setSmallIcon(R.drawable.ic_android_white_24dp)
                .build();
        startForeground(2,notification);
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        Log.e("SERVICE BLE", "Service STOPED");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            if (message.contains("moving")){ // if moving message received stop service (cut notification)
                stopSelf();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
