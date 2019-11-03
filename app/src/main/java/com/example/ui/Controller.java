package com.example.ui;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Console;

public class Controller{

    private Context context;
    private AppCompatActivity caller_activity;

    public Controller(Context context, AppCompatActivity caller){
        this.context = context;
        caller_activity = caller;
    }

    View.OnClickListener menu_pop = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(context,v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(menu_item_click);
        }
    };

    View.OnClickListener tag_settings_dialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((TagsActivity)caller_activity).startSettingsDialogFragment((TagsRecyclerAdapter.MyViewHolder)((View)v.getParent()).getTag());
        }
    };

    View.OnClickListener tag_create_dialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((TagsActivity)caller_activity).startCreateDialogFragment();
        }
    };


    static View.OnClickListener fragment_cancel_dialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((DialogFragment)v.getTag()).dismiss();
        }
    };

    static View.OnClickListener fragment_submit_tag_dialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((TagDialogFragment)v.getTag()).submitAndTerminate();
        }
    };



    PopupMenu.OnMenuItemClickListener menu_item_click = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch(item.getTitle().toString()){

                case "Settings":
                    ((MainActivity)caller_activity).startActivity(SettingsActivity.class);
                    break;

                case "My Tags":
                    ((MainActivity)caller_activity).startActivity(TagsActivity.class);
                    break;

                case "Catalog":
                    //TODO Implement catalog
                    break;

                default:
                    break;
            }

            return true;
        }
    };

    View.OnClickListener tag_list_item_selection_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(((TagObj)v.getTag()).selected == false){
                ((TagObj)v.getTag()).selected = true;
                ((CheckBox)v).setSelected(true);
            } else {
                ((TagObj)v.getTag()).selected = false;
                ((CheckBox)v).setSelected(false);
            }

        }
    };

    View.OnClickListener tag_list_item_alarm_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(((TagObj)v.getTag()).alarm == false){
                ((TagObj)v.getTag()).alarm = true;
                ((ImageButton)v).setForeground(ContextCompat.getDrawable(context, R.drawable.ringing_alarm));
            } else {
                ((TagObj)v.getTag()).alarm = false;
                ((ImageButton)v).setForeground(ContextCompat.getDrawable(context, R.drawable.silent_alarm));
            }

        }
    };


    public void handleOnItemSelected(MenuItem item){

        if(item.getItemId() == 16908332){//Handle return
            caller_activity.finish();
        }
        else if (item.getItemId() == R.id.delete_tag){
            ((TagsActivity)caller_activity).deleteSelected();
        }else if (item.getItemId() == R.id.create_tag) {
            TagDialogFragment createTag = new TagDialogFragment();
            createTag.show(caller_activity.getSupportFragmentManager(),"create tag");
        }
    }

}
