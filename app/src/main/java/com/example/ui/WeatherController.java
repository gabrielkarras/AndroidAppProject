package com.example.ui;

import android.content.Context;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.net.URL;

public class WeatherController {
    private final String Celcius = " °C";
    private final String Farenheit = " °F";
    private final String WindUnit = " Mph";
    private final String LiquidUnit = " inch";
    private FetchWeatherDetails WeatherDetails;
    private WeatherForecastInformation WeatherForecast;
    private Button button1;
    private Button button2;
    private Button button3;
    private TextView textField1;
    private TextView textField2;
    private TextView textField3;
    private TextView degrees_main;
    private TextView felt_degress;
    private TextView critical_parameter1_desc;
    private TextView critical_parameter2_desc;
    private TextView critical_parameter3_desc;
    private TextView critical_parameter4_desc;
    private TextView weather_status;
    private ImageView weather_status_icon;
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
                //TODO: might cause an issue with other async functions
                Thread.sleep(1000);

            }catch (Exception e){
                e.printStackTrace();
        }
        WeatherForecast = WeatherDetails.parseJSON();
    }

    private void setUpDisplayFields() {
        button1 = caller_activity.findViewById(R.id.suggestion_bttn1);
        button2 = caller_activity.findViewById(R.id.suggestion_bttn2);
        button3 = caller_activity.findViewById(R.id.suggestion_bttn3);

        textField1 = caller_activity.findViewById(R.id.sug1_title);
        textField2 = caller_activity.findViewById(R.id.sug2_title);
        textField3 = caller_activity.findViewById(R.id.sug3_title);

        degrees_main = caller_activity.findViewById(R.id.degrees_main);
        felt_degress = caller_activity.findViewById(R.id.felt_degress);
        critical_parameter1_desc = caller_activity.findViewById(R.id.critical_parameter1_desc);
        critical_parameter2_desc = caller_activity.findViewById(R.id.critical_parameter2_desc);
        critical_parameter3_desc = caller_activity.findViewById(R.id.critical_parameter3_desc);
        critical_parameter4_desc = caller_activity.findViewById(R.id.critical_parameter4_desc);

        weather_status = caller_activity.findViewById(R.id.weather_status);
        weather_status_icon = caller_activity.findViewById(R.id.weather_status_icon);
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


        degrees_main.setText((WeatherForecast.getDecimalAvgTemp() + Farenheit));
        felt_degress.setText(String.valueOf(WeatherForecast.getAvgTemp()));
        critical_parameter1_desc.setText(WeatherForecast.getWindSpeed() + WindUnit);
        critical_parameter2_desc.setText(WeatherForecast.getWindGustSpeed() + WindUnit);
        critical_parameter3_desc.setText(WeatherForecast.getSnowTotal() + LiquidUnit);
        critical_parameter4_desc.setText(WeatherForecast.getRainTotal() + LiquidUnit);

        weather_status.setText(WeatherForecast.getWeatherConditionPhrase());

        WeatherForecastInformation.WeatherCondition temp2 = WeatherForecast.getWeatherCondition();

        if (temp2 == WeatherForecastInformation.WeatherCondition.SUNNY)
            weather_status_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wether_partly_cloudy));
        else if (temp2 == WeatherForecastInformation.WeatherCondition.RAIN)
            weather_status_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icons8_stormy_weather_75));
        else if (temp2 == WeatherForecastInformation.WeatherCondition.SNOW)
            weather_status_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icons8_snow_storm_75));
    }
}
