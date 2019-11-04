package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ui.Controller;
import com.example.ui.R;

public class MainMenuFragmentOpen extends Fragment {

    private Controller controller;

    public MainMenuFragmentOpen() {
        super();
    }

    public MainMenuFragmentOpen(Controller controller) {
        super();
        this.controller = controller;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment_open,container,false);
        view.findViewById(R.id.tags_bttn).setOnClickListener(controller.menu_item_click);
        view.findViewById(R.id.settings_bttn).setOnClickListener(controller.menu_item_click);
        return view;
    }
}
