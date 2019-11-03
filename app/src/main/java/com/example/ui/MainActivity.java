package com.example.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Controller controller;
    private WeatherController WeatherController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new Controller(getApplicationContext(),this);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.main_menu)).setOnClickListener(controller.menu_pop);
        WeatherController = new WeatherController(getApplicationContext(),this);
        WeatherController.displayWeatherInformation();
    }

    public void startActivity(Class<?> T){
        Intent activity = new Intent(getApplicationContext(),T);
        startActivity(activity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        controller.handleOnItemSelected(item);
        return true;
    }
}

