package com.sn6266991.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sn6266991.fragment.SettingsFragment;

/**
 * 6266991
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
