package com.skyweather;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class Prefs extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            boolean isSetToTrue = prefs.getBoolean("checkbox", true);
            if (isSetToTrue) getPreferenceScreen().findPreference("list").setEnabled(false);
            else getPreferenceScreen().findPreference("list").setEnabled(true);
            final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("checkbox");

            checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isActive = (boolean) newValue;
                    if (isActive) getPreferenceScreen().findPreference("list").setEnabled(false);
                    else getPreferenceScreen().findPreference("list").setEnabled(true);
                    return true;
                }
            });
            Preference button =findPreference("info");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Toast.makeText(getActivity(),"App developed by GreenApps Studios Powered by Forecast.io ",Toast.LENGTH_LONG).show();
                    return true;
                    // Create the AlertDialog object and return it
                }
            });
        }
    }
}

