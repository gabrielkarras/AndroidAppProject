package com.example.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class TagsActivity extends AppCompatActivity {

    private Controller controller;
    private ArrayList<TagObj> myDataset;
    private TagsRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_tags);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controller = new Controller(getApplicationContext(),this);

        // Enable return button on tool bar and give it the back arrow icon.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.taglist_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        myDataset = new ArrayList<>();
        myDataset.add(new TagObj("Backpack",1,true));
        myDataset.add(new TagObj("Umbrella",2,true));
        myDataset.add(new TagObj("Car Keys",3,true));

        // specify an adapter (see also next example)
        mAdapter = new TagsRecyclerAdapter(myDataset,getApplicationContext(),controller);
        recyclerView.setAdapter(mAdapter);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tags_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        controller.handleOnItemSelected(item);
        return true;
    }

    public void deleteSelected(){
//        if (myDataset != null) {
//            int i = 0;
//            while (i < myDataset.size()) {
//                if (myDataset.get(i).selected) {
//                    myDataset.remove(i);
//                    mAdapter.notifyItemRemoved(i);
//                    i = 0;
//                } else {
//                    i++;
//                }
//            }
//        }
    }

    public void startActivity(Class<?> T){
        Intent activity = new Intent(getApplicationContext(),T);
        startActivity(activity);
    }

    public void terminate(){
        this.finish();
    }
}
