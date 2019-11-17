package com.example.ui;

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

public class TagsActivity extends AppCompatActivity implements DataLinker {

    private Controller controller;
    private ArrayList<TagObj> myDataset;
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

        myDataset = new ArrayList<>();

        //TODO retrieve current dataset
//      myDataset.add(new TagObj("Backpack",1,true));
//      myDataset.add(new TagObj("Umbrella",2,true));
//      myDataset.add(new TagObj("Car Keys",3,true));


        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(myDataset == null || myDataset.size() == 0){
            firstTimeUser = true;

            //Hide list elements
            recyclerView.setVisibility(View.INVISIBLE);

            // dynamically add the fragment by replacing the tags list
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragmentcontainer,newUserTag);
            fragmentTransaction.commit();

        } else {

            // specify an adapter
            mAdapter = new TagsRecyclerAdapter(myDataset, getApplicationContext(), controller);
            recyclerView.setAdapter(mAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInstance = menu;
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
        bundle.putSerializable("tagVal",myDataset.get(recyclerItemHandler.getAdapterPosition()));

        TagDialogFragment settingsDialog = new TagDialogFragment();
        settingsDialog.setArguments(bundle);

        settingsDialog.show(getSupportFragmentManager(),"Tag Settings Dialog");
    }

    public void startCreateDialogFragment(){
        TagDialogFragment settingsDialog = new TagDialogFragment();
        settingsDialog.show(getSupportFragmentManager(),"Tag Creation Dialog");
    }

    public void deleteSelected(){

        if (myDataset != null) {
            boolean changed = false;

            for(int i=0;i<myDataset.size();i++){
                Log.e("FFFFFFFFFF",myDataset.get(i).getName()+"  "+myDataset.get(i).selected);
            }

            do {
                changed = false;
                ListIterator<TagObj> iterator = myDataset.listIterator();
                while (iterator.hasNext()) {

                    TagObj temp = iterator.next();

                    if (temp.selected) {
                        int rm_item_index = iterator.nextIndex()-1;
                        iterator.remove();
                        mAdapter.notifyItemRemoved(rm_item_index);
                        changed = true;
                    }
                }
            }while(changed);

            for(int i=0;i<myDataset.size();i++){
                Log.e("FFFFFFFFFF",myDataset.get(i).getName()+"  "+myDataset.get(i).selected);
            }

            if(myDataset.size() == 0){
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
            }
        }

    }

    private int recordIndex(String address) {
        ListIterator<TagObj> it = myDataset.listIterator();
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
                myDataset.get(tagNumber).updateFields(data.getString("name").trim());
                mAdapter.notifyDataSetChanged();
            }
            //Create new record
            else {
                TagObj temp = new TagObj(data.getString("name").trim(),data.getString("address"));
                temp.selected = false;
                myDataset.add(temp);

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
                    mAdapter = new TagsRecyclerAdapter(myDataset, this, controller);
                    recyclerView.setAdapter(mAdapter);
                } else {

                    mAdapter.notifyDataSetChanged();
                }
            }

        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public String checkIfConflictsWithDataset(JSONObject data) {

        Iterator<TagObj> iterator = myDataset.iterator();
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
