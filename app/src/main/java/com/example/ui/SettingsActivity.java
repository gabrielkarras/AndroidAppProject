package com.example.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragment;

import fragments.MySettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    private Controller controller;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        controller = new Controller(getApplicationContext(),this);

        // Use the created toolbar as the app action bar and set it's title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        // Enable return button on tool bar and give it the back arrow icon.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        //dynamically add the fragment by replacing the placeholder framelayout
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new MySettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        controller.handleOnItemSelected(item);
        return true;
    }

    public void startActivity(Class<?> T){
        Intent activity = new Intent(getApplicationContext(),T);
        startActivity(activity);
    }


    public void terminate(){
        this.finish();
    }


}

