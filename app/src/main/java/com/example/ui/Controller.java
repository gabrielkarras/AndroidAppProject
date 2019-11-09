package com.example.ui;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import fragments.TagDialogFragment;

public class Controller{
    private Context context;
    private AppCompatActivity caller_activity;


    public Controller(Context context, AppCompatActivity caller){
        this.context = context;
        caller_activity = caller;
    }

    public View.OnClickListener tag_settings_dialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((TagsActivity)caller_activity).startSettingsDialogFragment((TagsRecyclerAdapter.MyViewHolder)((View)v.getParent()).getTag());
        }
    };

    public View.OnClickListener tag_create_dialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((TagsActivity)caller_activity).startCreateDialogFragment();
        }
    };


    public static View.OnClickListener fragment_cancel_dialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((DialogFragment)v.getTag()).dismiss();
        }
    };

    public static View.OnClickListener fragment_submit_tag_dialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((TagDialogFragment)v.getTag()).submitAndTerminate();
        }
    };


    public View.OnClickListener menu_bttn_click = new View.OnClickListener() {
        @Override
        public void onClick(View item) {
            ((MainActivity)caller_activity).openMenu();
        }
    };

    public View.OnClickListener outbounds_click = new View.OnClickListener() {
        @Override
        public void onClick(View item) {
            ((MainActivity)caller_activity).closeMenu();
        }
    };

    public View.OnClickListener suggestion_toggler = new View.OnClickListener() {
        @Override
        public void onClick(View item) {
            ((MainActivity)caller_activity).closeMenu();
            int fragment_holder_id = (Integer)item.getTag();

            switch((Integer)item.getTag()){
                case R.id.fragment_suggeston_1:

                    if (((MainActivity)caller_activity).explanation1_visible){
                        ((MainActivity)caller_activity).explanation1_visible = false;
                        ((MainActivity)caller_activity).closeExplanation(fragment_holder_id);
                    } else {
                        ((MainActivity)caller_activity).explanation1_visible = true;
                        ((MainActivity)caller_activity).openExplanation(fragment_holder_id);
                    }

                    break;
                case R.id.fragment_suggeston_2:
                    if (((MainActivity)caller_activity).explanation2_visible){
                        ((MainActivity)caller_activity).explanation2_visible = false;
                        ((MainActivity)caller_activity). closeExplanation(fragment_holder_id);
                    } else {
                        ((MainActivity)caller_activity).explanation2_visible = true;
                        ((MainActivity)caller_activity).openExplanation(fragment_holder_id);
                    }

                    break;
                case R.id.fragment_suggeston_3:
                    if (((MainActivity)caller_activity).explanation3_visible){
                        ((MainActivity)caller_activity).explanation3_visible = false;
                        ((MainActivity)caller_activity).closeExplanation(fragment_holder_id);
                    } else {
                        ((MainActivity)caller_activity).explanation3_visible = true;
                        ((MainActivity)caller_activity).openExplanation(fragment_holder_id);
                    }

                    break;
                default:
                    break;
            }
        }
    };

    public View.OnClickListener menu_item_click = new View.OnClickListener() {
        @Override
        public void onClick(View item) {

            switch(((Button)item).getText().toString()){

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
        }
    };

    public View.OnClickListener tag_list_item_selection_listener = new View.OnClickListener() {
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

    public View.OnClickListener tag_list_item_alarm_listener = new View.OnClickListener() {
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
