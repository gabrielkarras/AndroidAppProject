package com.example.ui;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

public class Controller {
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
            TagSettingsDialogFragment settingsDialog = new TagSettingsDialogFragment();
            settingsDialog.show(caller_activity.getSupportFragmentManager(),"Tag Settings Dialog");
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
                    break;

                default:
                    break;
            }

            return true;
        }
    };

    public void handleOnItemSelected(MenuItem item){

        if(item.getItemId() == 16908332){
            caller_activity.finish();
        }
        else if (item.getItemId() == R.id.delete_tag){
            ((TagsActivity)caller_activity).deleteSelected();
        }else if (item.getItemId() == R.id.create_tag) {
            TagSettingsDialogFragment createTag = new TagSettingsDialogFragment();
            createTag.show(caller_activity.getSupportFragmentManager(),"create tag");
        }
    }


}
