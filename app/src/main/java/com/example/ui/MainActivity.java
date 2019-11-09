package com.example.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.widget.Button;

import fragments.MainMenuFragmentClosed;
import fragments.MainMenuFragmentOpen;
import fragments.SuggestionExplanationClosed;
import fragments.SuggestionExplanationOpen;

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
        WeatherController = new WeatherController(getApplicationContext(),this);
        WeatherController.displayWeatherInformation();

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
}

