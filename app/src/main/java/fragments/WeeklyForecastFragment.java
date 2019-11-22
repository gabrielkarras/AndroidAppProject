package fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ui.MainActivity;
import com.example.ui.R;
import com.example.ui.StringRecyclerAdapter;
import com.example.ui.WeeklyItemsAdapter;

import org.json.JSONArray;

public class WeeklyForecastFragment extends Fragment {

    private RecyclerView recyclerView;
    private WeeklyItemsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private JSONArray weeklyforecast;
    private Context context;
    public WeeklyForecastFragment() {
        super();
    }

    public WeeklyForecastFragment(Context context, JSONArray weeklyforecast) {
        super();
        this.weeklyforecast = weeklyforecast;
        this.context = context;
    }

    public WeeklyForecastFragment(Context context) {
        super();
        this.weeklyforecast = new JSONArray();
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weekly_forecast_fragment,container,false);
        recyclerView = view.findViewById(R.id.weekly_forecast_recycler);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));

        if(weeklyforecast != null) {
            adapter = new WeeklyItemsAdapter(weeklyforecast, ((MainActivity)context).weatherController);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        return view;
    }

    public void updateWeeklyForecast(JSONArray weeklyforecast){
        this.weeklyforecast = weeklyforecast;
        if(recyclerView != null) {
            if (adapter == null) {
                adapter = new WeeklyItemsAdapter(this.weeklyforecast, ((MainActivity)context).weatherController);
                recyclerView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        }
    }

}
