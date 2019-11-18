package com.example.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StringRecyclerAdapter extends RecyclerView.Adapter<StringRecyclerAdapter.StringViewHolder> {

    private ArrayList<String> myDataset;
    private Context context;



    public static class StringViewHolder extends RecyclerView.ViewHolder {

        public TextView label;

        public StringViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.ble_source_addr);
        }
    }

    public StringRecyclerAdapter(ArrayList<String> myDataset, Context context) {
        this.myDataset = myDataset;
        this.context = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public StringRecyclerAdapter.StringViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stringlist_item_layout, parent, false);
        StringViewHolder vh = new StringViewHolder(v);
        v.setTag(vh);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BLE_SelectorActivity)v.getContext()).finishActivity(((TextView)v.findViewById(R.id.ble_source_addr)).getText().toString());
            }
        });

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(StringViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.label.setText(myDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}
