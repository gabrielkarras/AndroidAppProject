package com.example.ui;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import fragments.TagDialogFragment;
import fragments.newTagUserFragment;

import static com.example.ui.AppName.registeredTags;

public class TagsActivity extends AppCompatActivity implements DataLinker {

    private Controller controller;
    private TagsRecyclerAdapter mAdapter;

    private boolean firstTimeUser;
    private FragmentTransaction fragmentTransaction;
    private newTagUserFragment newUserTag;

    private RecyclerView recyclerView;
    private Menu menuInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_tags);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controller = new Controller(getApplicationContext(),this);
        newUserTag = new newTagUserFragment(controller);

        // Enable return button on tool bar and give it the back arrow icon.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        recyclerView = (RecyclerView) findViewById(R.id.taglist_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        if(registeredTags == null) {
            registeredTags = new ArrayList<>();
        }else{
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                AppName.invokeTrackingService();
            }
        }

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(registeredTags == null || registeredTags.size() == 0){
            firstTimeUser = true;

            //Hide list elements
            recyclerView.setVisibility(View.INVISIBLE);

            // dynamically add the fragment by replacing the tags list
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragmentcontainer,newUserTag);
            fragmentTransaction.commit();

        } else {
            // specify an adapter
            mAdapter = new TagsRecyclerAdapter(registeredTags, getApplicationContext(), controller);
            recyclerView.setAdapter(mAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInstance = menu;
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        getMenuInflater().inflate(R.menu.tags_menu,menu);

        //Hide menu options if first time creation screen
        if(firstTimeUser) {
            int menuSize = menuInstance.size();
            for (int i = 0; i < menuSize; i++) {
                menuInstance.getItem(i).setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        controller.handleOnItemSelected(item);
        return true;
    }

    public void startSettingsDialogFragment(TagsRecyclerAdapter.MyViewHolder recyclerItemHandler){
        Bundle bundle = new Bundle();
        bundle.putParcelable("tagVal",registeredTags.get(recyclerItemHandler.getAdapterPosition()));

        TagDialogFragment settingsDialog = new TagDialogFragment();
        settingsDialog.setArguments(bundle);

        settingsDialog.show(getSupportFragmentManager(),"Tag Settings Dialog");
    }

    public void startCreateDialogFragment(){
        TagDialogFragment settingsDialog = new TagDialogFragment();
        settingsDialog.show(getSupportFragmentManager(),"Tag Creation Dialog");
    }

    public void deleteSelected(){
        boolean deleted_any = false;
        if (registeredTags != null) {
            boolean changed = false;

            do {
                changed = false;
                ListIterator<TagObj> iterator = registeredTags.listIterator();
                while (iterator.hasNext()) {

                    TagObj temp = iterator.next();

                    if (temp.selected) {
                        int rm_item_index = iterator.nextIndex()-1;
                        iterator.remove();
                        mAdapter.notifyItemRemoved(rm_item_index);
                        changed = true;
                        deleted_any = true;
                    }
                }
            }while(changed);

            if(deleted_any){
                AppName.serviceController.updateAndRun(registeredTags);
                AppName.saveTrackedTagsList();
            }

            if(registeredTags.size() == 0){
                firstTimeUser = true;

                //Hide list elements
                recyclerView.setVisibility(View.INVISIBLE);
                int menuSize = menuInstance.size();
                for(int i =0;i<menuSize;i++){
                    menuInstance.getItem(i).setVisible(false);
                }

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragmentcontainer,newUserTag);
                fragmentTransaction.commit();

                AppName.serviceController.stopService();
                AppName.saveTrackedTagsList();
            }
        }

    }

    private int recordIndex(String address) {
        ListIterator<TagObj> it = registeredTags.listIterator();
        while(it.hasNext()){
            if (it.next().getTagAddress().equals(address)){
                return it.nextIndex() - 1;
            }
        }

        return -1;
    }

    @Override
    public void treatData(JSONObject data) {
        try {
            //Check if record exists and update the existing one
            int tagNumber  = recordIndex(data.getString("address"));

            if( tagNumber >= 0){
                registeredTags.get(tagNumber).updateFields(data.getString("name").trim());
                mAdapter.notifyDataSetChanged();
                AppName.saveTrackedTagsList();
            }
            //Create new record
            else {
                TagObj temp = new TagObj(data.getString("name").trim(),data.getString("address"));
                temp.selected = false;
                registeredTags.add(temp);

                if(firstTimeUser){
                    firstTimeUser = false;
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(newUserTag);
                    fragmentTransaction.commit();
                    fragmentTransaction.runOnCommit(new Runnable() {
                        @Override
                        public void run() {
                            //Hide list elements
                            recyclerView.setVisibility(View.VISIBLE);
                            int menuSize = menuInstance.size();
                            for(int i =0;i<menuSize;i++){
                                menuInstance.getItem(i).setVisible(true);
                            }
                        }
                    });

                    // specify an adapter
                    mAdapter = new TagsRecyclerAdapter(registeredTags, this, controller);
                    recyclerView.setAdapter(mAdapter);
                } else {

                    mAdapter.notifyDataSetChanged();
                }
            }

            AppName.serviceController.updateAndRun(registeredTags);
            AppName.saveTrackedTagsList();

        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public String checkIfConflictsWithDataset(JSONObject data) {

        Iterator<TagObj> iterator = registeredTags.iterator();
        try {
            while (iterator.hasNext()) {
                TagObj temp = iterator.next();
                if (temp.getTagAddress().equals(data.getString("address")) && !data.getString("actionItem").equals("UPDATE")){
                    return "Tag Address already registered.";
                }

                if (temp.getName().equals(data.getString("name").trim())){
                    return "Tag Name already taken.";
                }
            }
        } catch (JSONException e){
            return "Wrong Data Format.";
        }

        return null;
    }
}
