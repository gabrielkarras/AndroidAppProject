package com.example.ui;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.net.URL;

public class WeatherController {
    private FetchWeatherDetails WeatherDetails;
    private WeatherForecastInformation WeatherForecast;
    private Button button1;
    private Button button2;
    private Button button3;
    private TextView textField1;
    private TextView textField2;
    private TextView textField3;
    private AppCompatActivity caller_activity;
    private Context context;

    public WeatherController(Context context, AppCompatActivity caller)
    {
        this.context = context;
        caller_activity = caller;
    }


    public void setUpWeatherForecast()
    {
        URL weatherURL = NetworkUltility.buildURLForWeather();
        WeatherDetails = new FetchWeatherDetails();
        WeatherDetails.execute(weatherURL);

        try {
            //set time in mili
            Thread.sleep(1000);

        }catch (Exception e){
            e.printStackTrace();
        }
        WeatherForecast = WeatherDetails.parseJSON();
    }

    private void setUpDisplayFields() {
        button1 = caller_activity.findViewById(R.id.imageButton);
        button2 = caller_activity.findViewById(R.id.imageButton2);
        button3 = caller_activity.findViewById(R.id.imageButton3);

        textField1 = caller_activity.findViewById(R.id.sug1_title);
        textField2 = caller_activity.findViewById(R.id.sug2_title);
        textField3 = caller_activity.findViewById(R.id.sug3_title);
    }

    private void displayColdWeatherItems()
    {
        button1.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_coat_75));
        button2.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_winter_boots_75));
        button3.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_mitten_75));

        textField1.setText(WeatherForecast.ColdWeatherItems[0]);
        textField2.setText(WeatherForecast.ColdWeatherItems[1]);
        textField3.setText(WeatherForecast.ColdWeatherItems[2]);
    }

    private void displayChillingWeatherItems()
    {
        button1.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_jacket_75));
        button2.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_work_boot_75));
        button3.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_scarf_100));

        textField1.setText(WeatherForecast.ChillingWeatherItems[0]);
        textField2.setText(WeatherForecast.ChillingWeatherItems[1]);
        textField3.setText(WeatherForecast.ChillingWeatherItems[2]);
    }

    private void displayWarmWeatherItems()
    {
        button1.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_t_shirt_75));
        button2.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_sneakers_75));
        button3.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_long_shorts_75));

        textField1.setText(WeatherForecast.WarmWeatherItems[0]);
        textField2.setText(WeatherForecast.WarmWeatherItems[1]);
        textField3.setText(WeatherForecast.WarmWeatherItems[2]);
    }

    public void displayWeatherInformation()
    {
        setUpDisplayFields();
        setUpWeatherForecast();
        WeatherForecastInformation.WeatherType temp = WeatherForecast.getWeatherType();

        if (temp == WeatherForecastInformation.WeatherType.COLD)
            displayColdWeatherItems();
        else if (temp == WeatherForecastInformation.WeatherType.CHILLING)
            displayChillingWeatherItems();
        else
            displayWarmWeatherItems();


    }
}
