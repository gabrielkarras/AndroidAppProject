package com.example.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class BLE_SelectorActivity extends AppCompatActivity {


    private ArrayList<String> myDataset;
    private RecyclerView recyclerView;
    private StringRecyclerAdapter mAdapter;
    private Menu menuInstance;
    private LinearLayoutManager layoutManager;

    private AlertDialog alert;

    //private BLEscanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble__selector);

        Toolbar toolbar = findViewById(R.id.selector_toolbar);
        setSupportActionBar(toolbar);

        // Enable return button on tool bar and give it the back arrow icon.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        recyclerView = (RecyclerView) findViewById(R.id.selector_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        myDataset = new ArrayList<String>();

       // scanner = new BLEscanner(this);
        Log.e("AAAA","IM HERE");
        scanForBLE();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("update-UI-gatt"));
    }

    public void scanForBLE(){

        AlertDialog.Builder builder = new AlertDialog.Builder(BLE_SelectorActivity.this);
        builder.setTitle("Scanning For Nearby Devices");
        builder.setMessage("Please wait.");
        alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();


        //TODO scan for BLE for 3 seconds in a new thread. or not in a thread. because of alert we can block UI.
        for(int i =0; i<100;i++) {
            myDataset.add("AAA:DDD:AA:" + ((int) (Math.random() * 10))+""+ ((int) (Math.random() * 10))+""+ ((int) (Math.random() * 10)));
        }

        //scanner.startScan();
//        try{
//            Thread.sleep(3000);
//        }catch(Exception e){
//
//        }
        //scanner.stopScan();

        alert.dismiss();

        if(myDataset != null) {
            mAdapter = new StringRecyclerAdapter(myDataset, this);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInstance = menu;
        getMenuInflater().inflate(R.menu.ble_selector_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.Refresh){
            scanForBLE();
        } else {
            setResult(0);
            finish();
        }
        return true;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            debug.setText(message);
        }
    };

    public void finishActivity(String address){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",address);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
