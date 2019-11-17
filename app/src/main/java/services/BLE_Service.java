package services;

import android.app.IntentService;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.ui.R;
import com.example.ui.TagObj;

import java.util.ArrayList;

import static com.example.ui.AppName.MANDATORY_NOTIFICATION_CHANNEL__ID;
import static com.example.ui.AppName.TRACKER_NOTIFICATION_CHANNEL__ID;


public class BLE_Service extends IntentService {

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

    public BLE_Service (){
        super("BLE_Service");
        setIntentRedelivery(false);
    }

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

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        targetList = intent.getParcelableArrayListExtra("targetArrayList");
        //TODO scan for BLE devices
        // IF device in lost list, add it to target list.
        // IF device not found for more than N scans, put it into lost list from targetList.
        // INVOKE startLostNotification() or startFoundNotification()

        /// PLEASE DELTE THIS, ITS JUST FOR DEMO
        int pongo = 0;
        while(pongo < 100){
            try {
                if(pongo == 50){
                    startLostNotification(targetList.get(0).getName());
                }

                if(pongo == 90){
                    startFoundNotification(targetList.get(1).getName());
                }

                Thread.sleep(1000);
                pongo++;
            } catch(Exception e){
               // e.printStackTrace();
            }
        }
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
        super.onDestroy();
    }

}
