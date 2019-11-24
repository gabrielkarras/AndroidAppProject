package fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ui.Controller;
import com.example.ui.R;
import com.example.ui.WeatherController;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DailyForecastFragment extends Fragment {

    private WeatherController controller;
    public TextView mainTemp ;
    public TextView feltDegrees;
    public ImageView icon;

    private Context context;

    private String mainTemperatureFahrenheit = "10";
    private String feltTemperatureFahrenheit = "10";
    private String mainTemperatureCelsius = "10";
    private String feltTemperatureCelsius = "10";

    private int iconId = R.drawable.clear_day;

    public DailyForecastFragment() {
        super();
    }

    public DailyForecastFragment( Context context, WeatherController controller) {
        super();
        this.controller = controller;
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_forecast_fragment,container,false);
        mainTemp = view.findViewById(R.id.degrees_main);

        mainTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMetricSystem();
                controller.updateSystemUnits();
            }
        });

        feltDegrees = view.findViewById(R.id.felt_degress);
        icon = view.findViewById(R.id.weather_status_icon);

        return view;
    }



    public void switchMetricSystem(){
        if(controller.displayFahrenheit){
            mainTemp.setText(mainTemperatureCelsius);
            feltDegrees.setText(feltTemperatureCelsius);
            controller.displayFahrenheit = false; // i.e Celsius
        } else {
            mainTemp.setText(mainTemperatureFahrenheit);
            feltDegrees.setText(feltTemperatureFahrenheit);
            controller.displayFahrenheit = true;
        }
    }

    private double convertToCelsius(double temp) {
        double result;
        result = (temp - 32)*5/9;
        return Math.round(result*100)/100.0;
    }

    public void setDataFields(String mainDegrees, String feltDegrees, int imageResourceId){
        mainTemperatureFahrenheit = mainDegrees;
        feltTemperatureFahrenheit = feltDegrees;

        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        double mainTemp = 10;
        double feltTemp = 10;

        try {
            mainTemp = nf.parse(mainTemperatureFahrenheit.replace(WeatherController.Fahrenheit, "")).doubleValue();
            feltTemp = nf.parse(feltTemperatureFahrenheit.replace(WeatherController.Fahrenheit, "")).doubleValue();
        } catch(ParseException e){
            e.printStackTrace();
        }

        mainTemperatureCelsius = convertToCelsius(mainTemp) + WeatherController.Celcius;
        feltTemperatureCelsius = "" + convertToCelsius(feltTemp);

        iconId = imageResourceId;
        updateUI();
    }

    public void updateUI(){
        if (controller.displayFahrenheit){
            mainTemp.setText(mainTemperatureFahrenheit);
            feltDegrees.setText(feltTemperatureFahrenheit);
        } else {
            mainTemp.setText(mainTemperatureCelsius);
            feltDegrees.setText(feltTemperatureCelsius);
        }
        icon.setImageResource(iconId);
    }
}
