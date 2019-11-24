package com.example.ui;

import android.content.Context;
import android.nfc.FormatException;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.net.URL;
import java.text.DecimalFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.parsers.FactoryConfigurationError;

import fragments.DailyForecastFragment;

public class WeatherController {

    public static final String Celcius = " °C";
    public static final String Fahrenheit = " °F";
    private final String WindUnitImperial = " Mph";
    private final String WindUnitMetric = " Km/h";
    private final String DistanceUnitImperial = " Miles";
    private final String DistanceUnitMetric = " Km";

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

    private TextView cityName;
    private TextView dayAndDate;

    private TextView weather_status;
    private AppCompatActivity caller_activity;
    private Context context;
    public boolean displayFahrenheit = true;
    public boolean isNorthenHemisphere = true;
    //private DecimalFormat df = new DecimalFormat("#.##");

    private GregorianCalendar localDate;
    private String forecastTimeZone;
    private String forecastedDate;
    private String currentCityName = "montreal";

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

        cityName = caller_activity.findViewById(R.id.city_txt_main);
        dayAndDate = caller_activity.findViewById(R.id.date_txt_main);

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
                weatherForecast[i].setWeatherConditionPhrase(weeklyArray.getJSONObject(i).getString("summary"));

                if (weeklyArray.getJSONObject(i).has("precipType")){
                    weatherForecast[i].precipitationType = weeklyArray.getJSONObject(i).getString("precipType");
                    weatherForecast[i].precipitationProbability = weeklyArray.getJSONObject(i).getString("precipProbability");
                }
            }
        } catch (JSONException exc){
            exc.printStackTrace();
        }
    }

    private void updateForecastDate(){
        try {

            TimeZone tz = TimeZone.getTimeZone(forecastTimeZone);
            localDate = new GregorianCalendar();
            SimpleDateFormat destFormat = new SimpleDateFormat("EEEE,dd/MM/yyyy");
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
            currentCityName = weeklyForecast.getString("request_City");
            isNorthenHemisphere = weeklyForecast.getBoolean("isNorthHemisphere");
            updateForecastDate();
            getWeatherInfoFromJSON(weeklyForecast.getJSONArray("data"));
            displayWeatherUI(((MainActivity)caller_activity).forecastDayOffset);
            ((MainActivity)caller_activity).updateBackgroundImg();
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
            populateWeeklyFragment();
            updateNonFragmentVisuals();

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

    private void populateDailyFragment(int dailyWeatherIconDrawableID, int day){
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        String mainTemp = "";
        String feltTemp = "";

        mainTemp = nf.format(weatherForecast[day].getDecimalAvgTemp());
        feltTemp = nf.format(weatherForecast[day].getAvgTemp());

        ((MainActivity)caller_activity).dailyForecastFragment.setDataFields(mainTemp+ Fahrenheit,feltTemp,dailyWeatherIconDrawableID);
    }

    private void updateNonFragmentVisuals(){
        int day =((MainActivity)caller_activity).forecastDayOffset;
        String windSpeed = "";
        String windGustSpeed = "";

        if (displayFahrenheit) {
            windSpeed = weatherForecast[day].getWindSpeed() + WindUnitImperial;
            windGustSpeed = weatherForecast[day].getWindGustSpeed() + WindUnitImperial;
            critical_parameter3_desc.setText(((int)weatherForecast[day].getVisibility()) + DistanceUnitImperial);

        } else {
            windSpeed = convertToKm(weatherForecast[day].getWindSpeed()) + WindUnitMetric;
            windGustSpeed = convertToKm(weatherForecast[day].getWindGustSpeed()) + WindUnitMetric;
            critical_parameter3_desc.setText(((int)convertToKm(weatherForecast[day].getVisibility())) + DistanceUnitMetric);
        }
        weather_status.setText(weatherForecast[day].getWeatherConditionPhrase());

        critical_parameter1_desc.setText(windSpeed);
        critical_parameter2_desc.setText(windGustSpeed);
        critical_parameter4_desc.setText(((int)(weatherForecast[day].getHumidity()*100.0))+" %");
        cityName.setText(currentCityName);
        dayAndDate.setText(forecastedDate.replace(",",", "));

    }

    private void populateWeeklyFragment(){

        JSONArray weekHolder = new JSONArray();
        for (int i=0; i<7;i++){
            weekHolder.put(new JSONObject());
            try {

                TimeZone tz = TimeZone.getTimeZone(forecastTimeZone);
                localDate = new GregorianCalendar();
                SimpleDateFormat destFormat = new SimpleDateFormat("EEEE,dd/MM/yyyy");
                destFormat.setTimeZone(tz);
                localDate.add(Calendar.DATE,i);

                weekHolder.getJSONObject(i).put("weekday", destFormat.format( localDate.getTime()).split(",")[0]);
                weekHolder.getJSONObject(i).put("date", destFormat.format( localDate.getTime()).split(",")[1]);

                if(displayFahrenheit) {
                    weekHolder.getJSONObject(i).put("temperature", (int) (weatherForecast[i].getDecimalAvgTemp())+Fahrenheit);
                } else {
                    weekHolder.getJSONObject(i).put("temperature", (int) (convertToCelsius(weatherForecast[i].getDecimalAvgTemp()))+Celcius);
                }

                weekHolder.getJSONObject(i).put("summary",weatherForecast[i].getWeatherConditionPhrase());
                weekHolder.getJSONObject(i).put("iconID",getDrawableIdFromString(weatherForecast[i].getIconString()));

                if(weatherForecast[i].precipitationType != null){
                    double probability = (int)(Double.parseDouble(weatherForecast[i].precipitationProbability) * 100.0);
                    weekHolder.getJSONObject(i).put("rain",capitalizeFirstLetter(weatherForecast[i].precipitationType +", "+probability+"%"));
                }

            } catch(Exception e){
                e.printStackTrace();
            }
        }
        try {
            ((MainActivity) caller_activity).setSuggestionExplanation(weekHolder.getJSONObject(((MainActivity) caller_activity).forecastDayOffset).getString("temperature"));
        } catch(Exception e){
            e.printStackTrace();
        }

        ((MainActivity)caller_activity).weeklyForecastFragment.updateWeeklyForecast(weekHolder);
    }

    public void updateSystemUnits(){
        populateWeeklyFragment();
        if(!displayFahrenheit) {
            ((MainActivity) caller_activity).setSuggestionExplanation((int) convertToCelsius(weatherForecast[((MainActivity) caller_activity).forecastDayOffset].getAvgTemp()) + Celcius);
        } else {
            ((MainActivity) caller_activity).setSuggestionExplanation((int) weatherForecast[((MainActivity) caller_activity).forecastDayOffset].getAvgTemp() + Fahrenheit);
        }
        updateNonFragmentVisuals();
    }

    private String capitalizeFirstLetter(String input){
        StringBuilder sb = new StringBuilder();
        sb.append(input);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    private double convertToKm(double temp) {
        double result;
        result = temp * 1.60934;
        return Math.round(result*100)/100.0;
    }

    private double convertToCelsius(double temp) {
        double result;
        result = (temp - 32)*5/9;
        return Math.round(result*100)/100.0;
    }

    public void setCurrentDateTo(int day){
        ((MainActivity)caller_activity).forecastDayOffset = day;
        updateForecastDate();
        populateDailyFragment(getDrawableIdFromString(weatherForecast[day].getIconString()),day);
        updateNonFragmentVisuals();
        ((MainActivity)caller_activity).toggleWeeklyForecast();
    }
}
