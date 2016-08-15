package com.sn6266991.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sn6266991.R;

/**
 * Base activity that all activity classes in this application will inherit<br/>
 * This class listens for theme value change (the "theme" value in default {@link SharedPreferences})<br/>
 * and attempts to call {@link Activity#setTheme(int)}, followed with activity recreation
 * using {@link Activity#recreate()}
 */
class BaseActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "BaseActivity";

    /**
     * The instance of default {@link SharedPreferences} object of this app
     */
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        refreshTheme(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preference_key_theme))){
            String value = sharedPreferences.getString(key, null);
            Log.d(getClass().getSimpleName(), "onSharedPreferenceChanged: Theme changed: " + value);
            refreshTheme(true);
        }
    }

    /**
     * Refresh the app theme
     * @param needToRecreate whether activity recreation is needed
     */
    private void refreshTheme(boolean needToRecreate){
        String themeKey = getString(R.string.preference_key_theme);
        String themeValueBlue = getString(R.string.preference_value_theme_blue);
        String themeValueWhite = getString(R.string.preference_value_theme_white);

        String themeValue = sharedPreferences.getString(themeKey, themeValueBlue);

        if (themeValue.equals(themeValueBlue)){
            setTheme(R.style.BlueTheme);
        }
        else if (themeValue.equals(themeValueWhite)) {
            setTheme(R.style.WhiteTheme);
        }

        if (needToRecreate){
            recreate();
        }
    }

}
