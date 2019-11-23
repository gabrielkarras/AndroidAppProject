package com.example.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private TextView numberOfActiveTags;

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
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        if(registeredTags != null || registeredTags.size() != 0){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AppName.invokeTrackingService();
            }
        }

        openMenuFragment = new MainMenuFragmentOpen(controller);
        closedMenuFragment = new MainMenuFragmentClosed(controller);

        openSuggestionFragment = new SuggestionExplanationOpen();
        closedSuggestionFragment = new SuggestionExplanationClosed();

        numberOfActiveTags = findViewById(R.id.number_of_tracked_tags);

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

       findViewById(R.id.static_weather_holder).setBackgroundResource(getBackgroundFromDate());

        //Notify user that the notifications for this app are off
        if (!notificationsEnabled){
            displayDisabledNotificationsAlert();
        }


        weatherController = new WeatherController(getApplicationContext(),this);
        dailyForecastFragment = new DailyForecastFragment(this,weatherController);
        weeklyForecastFragment = new WeeklyForecastFragment(this);

        resultReceiver = new ForecastResultReceiver(new Handler(Looper.getMainLooper()));
        startForecastService();

        openDailyForecast();
        closeMenu();
    }
    public void setSuggestionExplanation(String explanation){
        openSuggestionFragment.updateTxt(explanation);
    }
    private int getBackgroundFromDate(){

        int month = Integer.parseInt(""+DateFormat.format("MM",new Date()));
        switch(month){
            case 12:
            case 1:
            case 2:
                return R.drawable.winter;
            case 3:
            case 4:
            case 5:
                return R.drawable.spring;
            case 6:
            case 7:
            case 8:
                return R.drawable.summer;
            case 9:
            case 10:
            case 11:
                return R.drawable.automn;
            default:
                break;
        }
        return R.drawable.winter;
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

        currentLocation = getSharedPreferences(this.getPackageName() + "_preferences", MODE_PRIVATE).getString("location_preference","Montreal");

        //Get new updated daily forecast
        if(resultReceiver != null) {
            startForecastService();
        }

        //Update display of tracked tags
        if (numberOfActiveTags != null){
            try{
                JSONArray jsonArray = new JSONArray(getSharedPreferences("forecasterAppTrackerList",Context.MODE_PRIVATE).getString("JSONTagArray",""));
                numberOfActiveTags.setText("Tags: "+jsonArray.length());
            } catch(JSONException e){
                e.printStackTrace();
            }
        }

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
            e.printStackTrace();
        }
    }

    public void toggleWeeklyForecast(){
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
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }

    private void reDraw()
    {
       // weatherController.displayWeatherInformation(PreferenceManager.getDefaultSharedPreferences(this).getString("location_preference", "Default"));
    }
}
