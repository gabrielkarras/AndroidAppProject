package com.example.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TagsRecyclerAdapter extends RecyclerView.Adapter<TagsRecyclerAdapter.MyViewHolder> {
    private ArrayList<TagObj> TagDataset;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox selected;
        public TextView label;
        public ImageButton alarmTrigger;
        public LinearLayout parentLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            selected = itemView.findViewById(R.id.tagCheckBox);
            label = itemView.findViewById(R.id.tagLabel);
            alarmTrigger = itemView.findViewById(R.id.tagAlarmTrigger);
            parentLayout = itemView.findViewById(R.id.taglist_item_parent);
        }
    }

    public TagsRecyclerAdapter(ArrayList<TagObj> myDataset, Context context) {
        TagDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TagsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.taglist_item_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final int pos = position;
        holder.label.setText(TagDataset.get(position).getName());

        final int elementPosition = holder.getAdapterPosition();

        holder.alarmTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TagDataset.get(elementPosition).alarm == false){
                    TagDataset.get(elementPosition).alarm = true;
                    ((ImageButton)v).setForeground(ContextCompat.getDrawable(context, R.drawable.ringing_alarm));
                } else {
                    TagDataset.get(elementPosition).alarm = false;
                    ((ImageButton)v).setForeground(ContextCompat.getDrawable(context, R.drawable.silent_alarm));
                }
            }
        });

        holder.selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TagDataset.get(elementPosition).selected == false){
                    TagDataset.get(elementPosition).selected = true;
                    ((CheckBox)v).setSelected(true);
                } else {
                    TagDataset.get(elementPosition).selected = false;
                    ((CheckBox)v).setSelected(false);
                }
            }
        });

        holder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("WUUUT","Open Popup for "+((TextView)v).getText());
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return TagDataset.size();
    }

}