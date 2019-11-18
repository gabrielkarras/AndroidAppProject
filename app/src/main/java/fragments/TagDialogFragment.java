package fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ui.BLE_SelectorActivity;
import com.example.ui.Controller;
import com.example.ui.DataLinker;
import com.example.ui.R;
import com.example.ui.TagObj;

import org.json.JSONException;
import org.json.JSONObject;

public class TagDialogFragment extends DialogFragment {

    private DataLinker dataLinker;
    private EditText name;
    private TextView address;
    private TextView discover_link;

    private String selectedDevice;

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

        name = ((EditText)view.findViewById(R.id.tag_name_field));
        address = ((TextView)view.findViewById(R.id.tag_address_value));
        discover_link = ((TextView)view.findViewById(R.id.ble_discovery_link));

        discover_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent devicePicker = new Intent(view.getContext(), BLE_SelectorActivity.class);
                startActivityForResult(devicePicker, 1);
            }
        });

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

            name.setText(passedData.getName());
            originalName = passedData.getName();
            address.setText(passedData.getTagAddress());
            discover_link.setVisibility(View.INVISIBLE);
        }

        //If no data passed, enter tag creation mode and start scanning for closest bluetooth
        else {
            createNew = true;
            discover_link.setVisibility(View.VISIBLE);
            address.setText("");
        }

        return view;
    }

    public void submitAndTerminate(){
        JSONObject data = new JSONObject();

        try {
            data.put("name", name.getText().toString());
            data.put("originalName", name.getText().toString());
            data.put("address", address.getText().toString());
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

        if(address.getText()== null || address.getText().length() == 0){
            return "Tag Address was not detected.";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            address.setText(data.getStringExtra("result"));
        }
    }
}
