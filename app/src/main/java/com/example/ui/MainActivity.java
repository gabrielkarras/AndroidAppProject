package com.example.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fragments.DailyForecastFragment;
import fragments.MainMenuFragmentClosed;
import fragments.MainMenuFragmentOpen;
import fragments.SuggestionExplanationClosed;
import fragments.SuggestionExplanationOpen;
import fragments.WeeklyForecastFragment;
import services.ForecastService;

import static com.example.ui.AppName.notificationsEnabled;
import static com.example.ui.AppName.registeredTags;

public class MainActivity extends AppCompatActivity {

    private final Context currentContext = this;
    private Controller controller;
    public WeatherController weatherController;
    private FragmentTransaction fragmentTransaction;
    private MainMenuFragmentOpen openMenuFragment;
    private MainMenuFragmentClosed closedMenuFragment;
    public DailyForecastFragment dailyForecastFragment;
    public WeeklyForecastFragment weeklyForecastFragment;

    private SuggestionExplanationOpen openSuggestionFragment;
    private SuggestionExplanationClosed closedSuggestionFragment;

    public boolean explanation1_visible;
    public boolean explanation2_visible;
    public boolean explanation3_visible;
    public boolean weeklyForecastVisible;

    private AlertDialog notificationsDisabled;
    private ForecastResultReceiver resultReceiver;

    private String currentLocation = "Montreal";
    public int forecastDayOffset = 0;

    private class ForecastResultReceiver extends ResultReceiver {

        public ForecastResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                return;
            }

            try{
                weatherController.updateWeatherInfo(new JSONObject(resultData.getString("JSONString")));
            } catch(JSONException exc){

            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new Controller(getApplicationContext(),this);

        dailyForecastFragment = new DailyForecastFragment(this,controller);

        JSONArray arrya = new JSONArray();
        for(int i =0; i< 7;i++){
            JSONObject ttmp = new JSONObject();
            try {
                ttmp.put("day", "Monday");
                ttmp.put("date", "14/11/2019");
                ttmp.put("summary", "Game Over ppl...");
                ttmp.put("temperature", "66666");
            }catch(Exception e){

            }
            arrya.put(ttmp);
        }
        weeklyForecastFragment = new WeeklyForecastFragment(this,arrya);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        if(registeredTags != null || registeredTags.size() != 0){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            AppName.invokeTrackingService();
        }
     //   WeatherController = new WeatherController(getApplicationContext(),this);
      //  WeatherController.displayWeatherInformation();

        //TESTING TrackerController
      //  final TrackerStatusController TrackerController = new TrackerStatusController(getApplicationContext(),this, WeatherController);
        //TrackerController.checkTrackerActionStatus();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    TrackerController.checkTrackerActionStatus();
//                }
//            }
//        }).start();
        /////////////////////////////

        openMenuFragment = new MainMenuFragmentOpen(controller);
        closedMenuFragment = new MainMenuFragmentClosed(controller);

        openSuggestionFragment = new SuggestionExplanationOpen();
        closedSuggestionFragment = new SuggestionExplanationClosed();

        openDailyForecast();
        closeMenu();

       // dailyForecastFragment.mainTemp.setOnClickListener(controller.outbounds_click);
        findViewById(R.id.weather_bar).setOnClickListener(controller.outbounds_click);
        findViewById(R.id.menu_wrapper).setOnClickListener(controller.outbounds_click);
        findViewById(R.id.weekly_forecast).setOnClickListener(controller.weeklyforecast_bttn_click);

        Button sugg1 = findViewById(R.id.suggestion_bttn1);
        sugg1.setTag(R.id.fragment_suggeston_1);
        sugg1.setOnClickListener(controller.suggestion_toggler);

        Button sugg2 = findViewById(R.id.suggestion_bttn2);
        sugg2.setTag(R.id.fragment_suggeston_2);
        sugg2.setOnClickListener(controller.suggestion_toggler);

        Button sugg3 = findViewById(R.id.suggestion_bttn3);
        sugg3.setTag(R.id.fragment_suggeston_3);
        sugg3.setOnClickListener(controller.suggestion_toggler);

        //Notify user that the notifications for this app are off
        if (!notificationsEnabled){
            displayDisabledNotificationsAlert();
        }

        weatherController = new WeatherController(getApplicationContext(),this);
        resultReceiver = new ForecastResultReceiver(new Handler(Looper.getMainLooper()));
        startForecastService();
    }

    private void startForecastService() {
        Intent intent = new Intent(this, ForecastService.class);
        intent.putExtra("receiver",resultReceiver);
        intent.putExtra("location", currentLocation);
        startService(intent);
    }

    public void openMenu(){
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_right,R.anim.exit_right);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.menu_holder_fragment,openMenuFragment);
            fragmentTransaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }catch (Exception e){

        }

        findViewById(R.id.menu_holder_fragment).bringToFront();
    }

    public void closeMenu(){
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_right,R.anim.exit_right);
            fragmentTransaction.replace(R.id.menu_holder_fragment,closedMenuFragment);
            fragmentTransaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }catch (Exception e){

        }
        findViewById(R.id.menu_holder_fragment).bringToFront();
    }

    public void startActivity(Class<?> T){
        closeMenu();
        Intent activity = new Intent(getApplicationContext(),T);
        startActivity(activity);
    }

    public void displayDisabledNotificationsAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Notifications Disabled!");
        builder.setMessage("You wont get daily suggestions and won't be notified about lost tags!");
        notificationsDisabled = builder.create();
        notificationsDisabled.setCanceledOnTouchOutside(true);

        notificationsDisabled.setButton(Dialog.BUTTON_NEGATIVE,"Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                notificationsDisabled.dismiss();
            }
        });

        notificationsDisabled.setButton(Dialog.BUTTON_POSITIVE,"Change Settings",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, currentContext.getPackageName());
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("app_package", currentContext.getPackageName());
                    intent.putExtra("app_uid", currentContext.getApplicationInfo().uid);
                } else {
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + currentContext.getPackageName()));
                }
                currentContext.startActivity(intent);

            }
        });

        notificationsDisabled.show();
    }

    public void openExplanation(int fragment_holder_id){
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_top,R.anim.exit_top);
            fragmentTransaction.replace(fragment_holder_id,openSuggestionFragment);
            fragmentTransaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }catch (Exception e){

        }
    }

    public void closeExplanation(){
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // fragmentTransaction.setCustomAnimations(R.anim.enter_right,R.anim.exit_right);
            fragmentTransaction.remove(openSuggestionFragment);
            fragmentTransaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }catch (Exception e){

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //weatherController.displayWeatherInformation(PreferenceManager.getDefaultSharedPreferences(this).getString("location_preference", "Default"));

        //If notifications enabled schedule next notification alarm
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()){
            notificationsEnabled = true;
        } else {
            notificationsEnabled = false;
        }
    }

    public void openDailyForecast(){
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_top,R.anim.exit_top);
            fragmentTransaction.add(R.id.forecast_holder, dailyForecastFragment);
            fragmentTransaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }catch (Exception e){

        }
    }

    public void openWeeklyForecast(){
        closeMenu();
        if(weeklyForecastVisible){
            weeklyForecastVisible = false;
            try {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_top,R.anim.exit_top);
                fragmentTransaction.remove(weeklyForecastFragment);
                fragmentTransaction.add(R.id.forecast_holder, dailyForecastFragment);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }catch (Exception e){

            }
        } else {
            weeklyForecastVisible = true;
            try {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_top,R.anim.exit_top);
                fragmentTransaction.remove(dailyForecastFragment);
                fragmentTransaction.add(R.id.forecast_holder, weeklyForecastFragment);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }catch (Exception e){

            }
        }
    }

    private void reDraw()
    {
       // weatherController.displayWeatherInformation(PreferenceManager.getDefaultSharedPreferences(this).getString("location_preference", "Default"));
    }
}

