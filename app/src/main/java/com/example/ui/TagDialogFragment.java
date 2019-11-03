package com.example.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class TagDialogFragment extends DialogFragment {

    private DataLinker dataLinker;
    private TextView status;
    private EditText name;
    private EditText id;
    private Switch switch_button;

    private boolean createNew = false;
    private String originalName;

    public TagDialogFragment() {
        super();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataLinker = (DataLinker)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tag_settings_dialog,container,false);

        status = ((TextView)view.findViewById(R.id.tag_status));
        name = ((EditText)view.findViewById(R.id.tag_name_field));
        id = ((EditText)view.findViewById(R.id.tag_id_value));
        switch_button = ((Switch)view.findViewById(R.id.tag_buzzer_switch));


        Button cancel = ((Button)view.findViewById(R.id.tag_cancel));
        cancel.setTag(this);
        cancel.setOnClickListener(Controller.fragment_cancel_dialog);

        Button submit = ((Button)view.findViewById(R.id.tag_ok));
        submit.setTag(this);
        submit.setOnClickListener(Controller.fragment_submit_tag_dialog);

        //If tag object passed populate and enter edit mode
        if (this.getArguments() != null){

            TagObj passedData = (TagObj)(this.getArguments().get("tagVal"));
            createNew = false;

            status.setText(passedData.getStatus());
            name.setText(passedData.getName());
            originalName = passedData.getName();
            id.setText(Long.toString(passedData.getTagID()));
            switch_button.setChecked(passedData.isBuzzerEnabled());
            initiateStatusUpdate();
        }

        //If no data passed, enter tag creation mode and start scanning for closest bluetooth
        else {
            createNew = true;
            status.setText("Searching For Tags");
            initiateTagInitializationAttempts();
        }

        return view;
    }

    public void submitAndTerminate(){
        JSONObject data = new JSONObject();

        try {
            data.put("name", name.getText().toString());
            data.put("originalName", name.getText().toString());
            data.put("id", Long.parseLong(id.getText().toString()));
            data.put("buzzable", switch_button.isChecked());
            data.put("status", status.getText().toString());
            data.put("actionItem",(createNew) ? "CREATE":"UPDATE" );

        } catch(JSONException e){
            e.printStackTrace();
        }


        String error = getErrorReason(data);
        if(error != null && error.length() != 0){
            final AlertDialog alert = new AlertDialog.Builder((Context)dataLinker).create();
            alert.setTitle("Unable to Create Item");

            alert.setMessage(error);

            alert.setButton(Dialog.BUTTON_POSITIVE,"OK",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alert.dismiss();
                }
            });

            alert.show();

        } else {
            dataLinker.treatData(data);
            this.dismiss();
        }
    }

    public String getErrorReason(JSONObject data){

        if(id.getText()== null || id.getText().length() == 0){
            return "Tag ID was not detected.";
        }

        if(name.getText()== null || name.getText().length() == 0){
            return "Tag Name is empty.";
        }

        if(originalName==null || originalName.length() == 0 || !originalName.equals(name.getText().toString())) {
            return dataLinker.checkIfConflictsWithDataset(data);
        } else {
            return null;
        }

    }

    public void initiateStatusUpdate(){
        //TODO async BLUETOOTH and WIFI tag status reading
        //TODO update dialog UI
    }


    public void initiateTagInitializationAttempts(){
        id.setText("10100001");
        //TODO async BLUETOOTH scan to get a tag ID.
        //TODO should we do also wifi-on-same-network scan?
    }

}
