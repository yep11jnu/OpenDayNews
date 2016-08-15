package com.sn6266991.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.sn6266991.R;
import com.sn6266991.SyncManager;
import com.sn6266991.Toolbox;

/**
 * 6266991
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        getPreferenceScreen().getPreference(2).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toolbox.showNotification(getActivity());
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preference_key_theme))){
            String value = sharedPreferences.getString(key, null);
            Log.d(TAG, "onSharedPreferenceChanged: Theme changed: " + value);
        }
        else if (key.equals(getString(R.string.preference_key_sync_enabled)) || key.equals(getString(R.string.preference_key_sync_interval))){
            SyncManager.scheduleNext(getActivity());
        }
    }

}
