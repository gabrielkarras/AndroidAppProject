package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ui.Controller;
import com.example.ui.R;

public class MainMenuFragmentClosed extends Fragment {

    private Controller controller;

    public MainMenuFragmentClosed() {
        super();
    }
    public MainMenuFragmentClosed(Controller controller) {
        super();
        this.controller = controller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment_closed,container,false);

        Button theButton = (Button)view.findViewById(R.id.main_menu_closed_bttn);
        if(theButton !=  null && controller != null){
            theButton.setOnClickListener(controller.menu_bttn_click);
        }
        return view;
    }
}
