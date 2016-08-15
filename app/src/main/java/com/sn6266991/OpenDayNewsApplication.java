package com.sn6266991;

import android.app.Application;

import com.sn6266991.database.DatabaseManager;

/**
 * 6266991
 */
public class OpenDayNewsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Some initializations
        DatabaseManager.initialize(this);
        SyncManager.scheduleNext(this);
    }
}
