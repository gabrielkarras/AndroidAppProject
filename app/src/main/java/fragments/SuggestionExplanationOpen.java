package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ui.R;

public class SuggestionExplanationOpen extends Fragment {

    private TextView explanataion;
    private String explTxt = "EXPLANATION";

    public SuggestionExplanationOpen() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.suggestion_explanation_open,container,false);
        explanataion = (TextView)view.findViewById(R.id.reason_1);
        explanataion.setText(explTxt);
        return view;
    }

    public void updateTxt(String explanation){
        explTxt = explanation;
    }
}
