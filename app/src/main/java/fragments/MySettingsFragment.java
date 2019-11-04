package fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.ui.R;

public class MySettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.general_settings, rootKey);
    }
}
