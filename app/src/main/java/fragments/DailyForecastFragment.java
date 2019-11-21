package fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ui.Controller;
import com.example.ui.R;

import org.json.JSONObject;

public class DailyForecastFragment extends Fragment {

    private JSONObject dailyForecast;
    private Controller controller;
    public TextView mainTemp;
    public TextView feltDegrees;
    public ImageView icon;

    private Context context;

    public DailyForecastFragment() {
        super();
    }

    public DailyForecastFragment( Context context, Controller controller) {
        super();
        this.controller = controller;
        this.context = context;
    }

    public DailyForecastFragment(Context context, Controller controller, JSONObject dailyForecast) {
        super();
        this.dailyForecast = dailyForecast;
        this.controller = controller;
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_forecast_fragment,container,false);
        mainTemp = view.findViewById(R.id.degrees_main);
        mainTemp.setOnClickListener(controller.setUpClickToChangeDegreeTypeUI);
        feltDegrees = view.findViewById(R.id.felt_degress);
        icon = view.findViewById(R.id.weather_status_icon);

        return view;
    }

    public void updateUI(String mainDegrees, String feltDegrees, int imageResourceId){
        mainTemp.setText(mainDegrees);
        this.feltDegrees.setText(feltDegrees);
        icon.setImageResource(imageResourceId);
    }
}
