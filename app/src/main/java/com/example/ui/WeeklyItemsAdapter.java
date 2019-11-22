package com.example.ui;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeeklyItemsAdapter extends RecyclerView.Adapter<WeeklyItemsAdapter.dayViewHolder> {

    private JSONArray myDataset;
    private WeatherController controller;

    public static class dayViewHolder extends RecyclerView.ViewHolder {

        public TextView day_title;
        public TextView dorecast_date;
        public TextView forecast_summary;
        public ImageView forcast_icon;
        public TextView felt_temperature;
        public TextView rain;
        public TextView snow;
        public LinearLayout layout;


        public dayViewHolder(View itemView) {
            super(itemView);

            day_title = itemView.findViewById(R.id.day_title);
            dorecast_date = itemView.findViewById(R.id.forecast_date);
            forecast_summary = itemView.findViewById(R.id.forecast_summary);
            forcast_icon = itemView.findViewById(R.id.forecastLogo);
            felt_temperature = itemView.findViewById(R.id.felt_temperature);
            rain = itemView.findViewById(R.id.rain);
            snow = itemView.findViewById(R.id.snow);
            layout = itemView.findViewById(R.id.day_item_holder);
        }
    }

    public WeeklyItemsAdapter(JSONArray myDataset, WeatherController controller) {
        this.myDataset = myDataset;
        this.controller = controller;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WeeklyItemsAdapter.dayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekday_item, parent, false);
        dayViewHolder vh = new dayViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(dayViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            JSONObject temp = myDataset.getJSONObject(position);

            holder.day_title.setText(temp.getString("weekday"));
            holder.dorecast_date.setText(temp.getString("date"));
            holder.forecast_summary.setText(temp.getString("summary"));
            holder.forecast_summary.setMovementMethod(new ScrollingMovementMethod());
            holder.felt_temperature.setText(temp.getString("temperature"));
            holder.forcast_icon.setImageResource(Integer.parseInt(temp.getString("iconID")));

            final int pos = position;
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.setCurrentDateTo(pos);
                }
            });

            if (temp.has("rain")) {
                holder.rain.setText(temp.getString("rain"));
            } else {
                holder.rain.setVisibility(View.GONE);
            }

            if(temp.has("snow")) {
                holder.snow.setText(temp.getString("snow"));
            } else {
                holder.snow.setVisibility(View.GONE);
            }

        } catch(Exception e){
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myDataset.length();
    }

}

