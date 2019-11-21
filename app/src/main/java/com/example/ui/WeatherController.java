package com.example.ui;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import fragments.DailyForecastFragment;

public class WeatherController {

    private final String Celcius = " °C";
    private final String Fahrenheit = " °F";
    private final String WindUnitImperial = " Mph";
    private final String WindUnitMetric = " Km/h";
    private final String LiquidUnitImperial = " inch";
    private final String LiquidUnitMetric = " cm";

    private WeatherForecastInformation [] weatherForecast;

    private Button button1;
    private Button button2;
    private Button button3;

    private TextView textField1;
    private TextView textField2;
    private TextView textField3;

    private TextView critical_parameter1_desc;
    private TextView critical_parameter2_desc;
    private TextView critical_parameter3_desc;
    private TextView critical_parameter4_desc;

    private TextView weather_status;
    private AppCompatActivity caller_activity;
    private Context context;
    private boolean displayFahrenheit = true;
    private DecimalFormat df = new DecimalFormat("#.##");

    private GregorianCalendar localDate;
    private String forecastTimeZone;
    private String forecastedDate;

    public WeatherController(Context context, AppCompatActivity caller) {
        this.context = context;
        caller_activity = caller;
        setUpDisplayFields();
    }

    private void setUpDisplayFields() {
        button1 = caller_activity.findViewById(R.id.suggestion_bttn1);
        button2 = caller_activity.findViewById(R.id.suggestion_bttn2);
        button3 = caller_activity.findViewById(R.id.suggestion_bttn3);

        textField1 = caller_activity.findViewById(R.id.sug1_title);
        textField2 = caller_activity.findViewById(R.id.sug2_title);
        textField3 = caller_activity.findViewById(R.id.sug3_title);


        critical_parameter1_desc = caller_activity.findViewById(R.id.critical_parameter1_desc);
        critical_parameter2_desc = caller_activity.findViewById(R.id.critical_parameter2_desc);
        critical_parameter3_desc = caller_activity.findViewById(R.id.critical_parameter3_desc);
        critical_parameter4_desc = caller_activity.findViewById(R.id.critical_parameter4_desc);

        weather_status = caller_activity.findViewById(R.id.weather_status);

    }

    private void displayColdWeatherItems(int day) {
        button1.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_coat_75));
        button2.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_winter_boots_75));
        button3.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_mitten_75));

        textField1.setText(weatherForecast[day].ColdWeatherItems[0]);
        textField2.setText(weatherForecast[day].ColdWeatherItems[1]);
        textField3.setText(weatherForecast[day].ColdWeatherItems[2]);
    }

    private void displayChillingWeatherItems(int day) {
        button1.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_jacket_75));
        button2.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_work_boot_75));
        button3.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_scarf_100));

        textField1.setText(weatherForecast[day].ChillingWeatherItems[0]);
        textField2.setText(weatherForecast[day].ChillingWeatherItems[1]);
        textField3.setText(weatherForecast[day].ChillingWeatherItems[2]);
    }

    private void displayWarmWeatherItems(int day) {
        button1.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_t_shirt_75));
        button2.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_sneakers_75));
        button3.setForeground(ContextCompat.getDrawable(context, R.drawable.icons8_long_shorts_75));

        textField1.setText(weatherForecast[day].WarmWeatherItems[0]);
        textField2.setText(weatherForecast[day].WarmWeatherItems[1]);
        textField3.setText(weatherForecast[day].WarmWeatherItems[2]);
    }

    private void getWeatherInfoFromJSON(JSONArray weeklyArray){
        if(weatherForecast == null){
            weatherForecast = new WeatherForecastInformation[weeklyArray.length()];
        }
        try {
            for(int i = 0; i < weeklyArray.length(); i++){
                weatherForecast[i] = new WeatherForecastInformation();

                //Extra info
                weatherForecast[i].setForecastTime(weeklyArray.getJSONObject(i).getLong("time"));
                weatherForecast[i].setIconString(weeklyArray.getJSONObject(i).getString("icon"));
                weatherForecast[i].setHumidity(weeklyArray.getJSONObject(i).getDouble("humidity"));
                weatherForecast[i].setVisibility(weeklyArray.getJSONObject(i).getDouble("visibility"));


                //Defaults by Khoi
                weatherForecast[i].setMaxtemp(weeklyArray.getJSONObject(i).getDouble("apparentTemperatureHigh"));
                weatherForecast[i].setMintemp(weeklyArray.getJSONObject(i).getDouble("apparentTemperatureLow"));
                weatherForecast[i].setDecimalMaxtemp(weeklyArray.getJSONObject(i).getDouble("temperatureHigh"));
                weatherForecast[i].setDecimalMintemp(weeklyArray.getJSONObject(i).getDouble("temperatureLow"));
                weatherForecast[i].setWindSpeed(weeklyArray.getJSONObject(i).getDouble("windSpeed"));
                weatherForecast[i].setWindGustSpeed(weeklyArray.getJSONObject(i).getDouble("windGust"));
                weatherForecast[i].setWeatherCondition(weeklyArray.getJSONObject(i).getString("precipType"));
                weatherForecast[i].setWeatherConditionPhrase(weeklyArray.getJSONObject(i).getString("summary"));
            }
        } catch (JSONException exc){
            exc.printStackTrace();
        }
    }

    private void updateForecastDate(){
        try {

            TimeZone tz = TimeZone.getTimeZone(forecastTimeZone);
            localDate = new GregorianCalendar();
            SimpleDateFormat destFormat = new SimpleDateFormat("EEEE;dd/MM/yyyy");
            destFormat.setTimeZone(tz);
            localDate.add(Calendar.DATE, ((MainActivity)caller_activity).forecastDayOffset);
            forecastedDate = destFormat.format( localDate.getTime());

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateWeatherInfo(JSONObject weeklyForecast) {

        try {
            forecastTimeZone = weeklyForecast.getString("timezone");
            updateForecastDate();
            getWeatherInfoFromJSON(weeklyForecast.getJSONArray("data"));//.getJSONObject(((MainActivity)caller_activity).forecastDayOffset)
            displayWeatherUI(((MainActivity)caller_activity).forecastDayOffset);
        } catch(JSONException exc){
            exc.printStackTrace();
        }
    }

    private void displayWeatherUI(int day) {

        if (weatherForecast != null) {
            WeatherForecastInformation.WeatherType temp = weatherForecast[day].getWeatherType();

            if (temp == WeatherForecastInformation.WeatherType.COLD)
                displayColdWeatherItems(day);
            else if (temp == WeatherForecastInformation.WeatherType.CHILLING)
                displayChillingWeatherItems(day);
            else
                displayWarmWeatherItems(day);

            populateDailyFragment(getDrawableIdFromString(weatherForecast[day].getIconString()),day);

//                critical_parameter3_desc.setText(WeatherForecast.getSnowTotal() + LiquidUnit);
//                critical_parameter4_desc.setText(WeatherForecast.getRainTotal() + LiquidUnit);
            critical_parameter3_desc.setText("NaN");
            critical_parameter4_desc.setText("NaN");

            weather_status.setText(weatherForecast[day].getWeatherConditionPhrase());

        }
    }

    private int getDrawableIdFromString(String drawableName){
        switch(drawableName){
            case "clear-day":
                return R.drawable.clear_day;
            case "clear-night":
                return R.drawable.clear_night;
            case "rain":
                return R.drawable.rain;
            case "snow":
                return R.drawable.snow;
            case "sleet":
                return R.drawable.sleet;
            case "wind":
                return R.drawable.wind;
            case "fog":
                return R.drawable.fog;
            case "cloudy":
                return R.drawable.cloudy;
            case "partly-cloudy-day":
                return R.drawable.partly_cloudy_day;
            case "partly-cloudy-night":
                return R.drawable.partly_cloudy_night;
            case "hail":
                return R.drawable.hail;
            case "thunderstorm":
                return R.drawable.thunderstorm;
            case "tornado":
                return R.drawable.tornado;
            default:
                break;
        }
        return R.drawable.not_found;
    }

    public void updateDailyFragment(int day){

    }

    private void populateDailyFragment(int dailyWeatherIconDrawableID, int day){
        String mainTemp = "";
        String feltTemp = "";
        String windSpeed = "";
        String windGustSpeed = "";

        if (displayFahrenheit) {
            mainTemp = df.format(weatherForecast[day].getDecimalAvgTemp()) + Fahrenheit;
            feltTemp = df.format(weatherForecast[day].getAvgTemp());
            windSpeed = weatherForecast[day].getWindSpeed() + WindUnitImperial;
            windGustSpeed = weatherForecast[day].getWindGustSpeed() + WindUnitImperial;

        } else {

            mainTemp = df.format(convertToCelsius(weatherForecast[day].getDecimalAvgTemp())) + Celcius;
            feltTemp = df.format(convertToCelsius(weatherForecast[day].getAvgTemp()));
            windSpeed = convertToKmH(weatherForecast[day].getWindSpeed()) + WindUnitMetric;
            windGustSpeed = convertToKmH(weatherForecast[day].getWindGustSpeed()) + WindUnitMetric;
        }

        ((MainActivity)caller_activity).dailyForecastFragment.updateUI(mainTemp,feltTemp,dailyWeatherIconDrawableID);
        critical_parameter1_desc.setText(windSpeed);
        critical_parameter2_desc.setText(windGustSpeed);
    }

    public boolean isGonnaSnow(int day) {
        return weatherForecast[day].isGonnaSnow();
    }

    public boolean isGonnaRain(int day) {
        return weatherForecast[day].isGonnaRain();
    }

    private double convertToCelsius(double temp) {
        double result;
        result = (temp - 32)*5/9;
        return Math.round(result*100)/100.0;
    }

    private double convertToKmH(double temp) {
        double result;
        result = temp * 1.60934;
        return Math.round(result*100)/100.0;
    }

    public void switchTempUnit() {
        displayFahrenheit = !displayFahrenheit;
        displayWeatherUI(((MainActivity)caller_activity).forecastDayOffset);
    }

}
