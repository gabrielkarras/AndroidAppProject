package com.example.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fragments.MainMenuFragmentClosed;
import fragments.MainMenuFragmentOpen;
import fragments.SuggestionExplanationClosed;
import fragments.SuggestionExplanationOpen;

import static com.example.ui.AppName.registeredTags;

public class MainActivity extends AppCompatActivity {
    private Controller controller;
    private WeatherController WeatherController;
    private FragmentTransaction fragmentTransaction;
    private MainMenuFragmentOpen openMenuFragment;
    private MainMenuFragmentClosed closedMenuFragment;

    private SuggestionExplanationOpen openSuggestionFragment;
    private SuggestionExplanationClosed closedSuggestionFragment;

    public boolean explanation1_visible;
    public boolean explanation2_visible;
    public boolean explanation3_visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new Controller(getApplicationContext(),this);

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
        WeatherController = new WeatherController(getApplicationContext(),this);
        setUpUI();

        openMenuFragment = new MainMenuFragmentOpen(controller);
        closedMenuFragment = new MainMenuFragmentClosed(controller);

        openSuggestionFragment = new SuggestionExplanationOpen();
        closedSuggestionFragment = new SuggestionExplanationClosed();

        closeMenu();

        findViewById(R.id.daily_forecast_fragment).setOnClickListener(controller.outbounds_click);
        findViewById(R.id.weather_bar).setOnClickListener(controller.outbounds_click);
        findViewById(R.id.menu_wrapper).setOnClickListener(controller.outbounds_click);

        Button sugg1 = findViewById(R.id.suggestion_bttn1);
        sugg1.setTag(R.id.fragment_suggeston_1);
        sugg1.setOnClickListener(controller.suggestion_toggler);

        Button sugg2 = findViewById(R.id.suggestion_bttn2);
        sugg2.setTag(R.id.fragment_suggeston_2);
        sugg2.setOnClickListener(controller.suggestion_toggler);

        Button sugg3 = findViewById(R.id.suggestion_bttn3);
        sugg3.setTag(R.id.fragment_suggeston_3);
        sugg3.setOnClickListener(controller.suggestion_toggler);

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        WeatherController.displayWeatherInformation(PreferenceManager.getDefaultSharedPreferences(this).getString("location_preference", "Default"));
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

    public void openExplanation(int fragment_holder_id){
        registeredTags.remove(1);
        registeredTags.add(new TagObj("ZAPAPUSTA","HELLLLLLO"));

        AppName.serviceController.updateTrackingTagList(registeredTags);
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_top,R.anim.exit_top);
            fragmentTransaction.replace(fragment_holder_id,openSuggestionFragment);
            fragmentTransaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }catch (Exception e){

        }
    }

    public void closeExplanation(int fragment_holder_id){
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // fragmentTransaction.setCustomAnimations(R.anim.enter_right,R.anim.exit_right);
            fragmentTransaction.remove(openSuggestionFragment);
            fragmentTransaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }catch (Exception e){

        }

    }

    private void setUpUI()
    {
        TextView degrees_main;
        degrees_main = findViewById(R.id.degrees_main);
        degrees_main.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                WeatherController.switchTempUnit();
                onResume();
            }
        });
    }
}

