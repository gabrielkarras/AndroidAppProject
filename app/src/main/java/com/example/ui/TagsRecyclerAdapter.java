package com.example.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TagsRecyclerAdapter extends RecyclerView.Adapter<TagsRecyclerAdapter.MyViewHolder> {
    private ArrayList<TagObj> TagDataset;
    private Controller controller;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox selected;
        public TextView label;
        public LinearLayout parentLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            selected = itemView.findViewById(R.id.tagCheckBox);
            label = itemView.findViewById(R.id.tagLabel);
            parentLayout = itemView.findViewById(R.id.taglist_item_parent);
        }
    }


    public TagsRecyclerAdapter(ArrayList<TagObj> myDataset, Context context, Controller controller) {
        TagDataset = myDataset;
        this.context = context;
        this.controller = controller;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public TagsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.taglist_item_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        v.setTag(vh);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.label.setText(TagDataset.get(position).getName());
        holder.selected.setTag(TagDataset.get(position));
        holder.selected.setChecked(TagDataset.get(position).selected);
        holder.selected.setOnCheckedChangeListener(controller.tag_list_item_selection_listener);
        holder.label.setOnClickListener(controller.tag_settings_dialog);

    }

    public void itemRemoved(int id){
        this.TagDataset.remove(id);
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return TagDataset.size();
    }

}