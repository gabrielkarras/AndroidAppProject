package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ui.Controller;
import com.example.ui.R;

public class newTagUserFragment extends Fragment {

    private Controller controller;

    public newTagUserFragment() {
        super();
    }
    public newTagUserFragment(Controller controller) {
        super();
        this.controller = controller;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tag_first_time_user,container,false);
        ((Button)view.findViewById(R.id.new_tag_first_user)).setOnClickListener(controller.tag_create_dialog);
        return view;
    }

}
