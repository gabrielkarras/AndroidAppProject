package com.example.ui;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.ui.R;

public class Controller {
    Context context;

    View.OnClickListener menu_pop = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(context,v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(menu_item_click);
        }
    };

    PopupMenu.OnMenuItemClickListener menu_item_click = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.e("MenuClick", "Clicked "+item.getTitle());
            return true;
        }
    };
}
