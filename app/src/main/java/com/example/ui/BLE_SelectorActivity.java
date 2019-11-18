package com.example.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private AlertDialog alert;
    private LinearLayoutManager layoutManager;

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
    }

    public void scanForBLE(){
        alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Scanning For Nearby Devices");
        alert.setMessage("Please wait.");
        alert.setCanceledOnTouchOutside(false);
        alert.show();

        //TODO scan for BLE for 3 seconds in a new thread. or not in a thread. because of alert we can block UI.
        for(int i =0; i<100;i++) {
            myDataset.add("AAA:DDD:AA:D" + ((int) (Math.random() * 10)));
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

    public void finishActivity(String address){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",address);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
