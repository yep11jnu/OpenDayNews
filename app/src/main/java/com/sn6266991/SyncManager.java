package com.sn6266991;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sn6266991.receiver.SyncReceiver;

import java.util.Date;

/**
 * 6266991
 */
public class SyncManager {

    private static final String TAG = "SyncManager";
    private static final int RC_SYNC = 2198;
    private static final boolean DEBUGGING = false;

    /**
     * Schedule the next fetching task. When called, this will take the saved "sync_interval" value
     * in the default {@link SharedPreferences} as <tt>x</tt> and schedule the fetching task to run
     * in the next <tt>x</tt> minutes from now
     */
    public static void scheduleNext(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isSyncEnabled = preferences.getBoolean(context.getString(R.string.preference_key_sync_enabled), true);
        if (isSyncEnabled) {
            int interval = preferences.getInt(context.getString(R.string.preference_key_sync_interval), 5) * 60000;

            // This line for debugging
            if (DEBUGGING) {
                interval = 10000;
            }

            if (interval > 0) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pi = createPendingIntent(context);

                long nextAlarm = System.currentTimeMillis() + interval;
                alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarm, pi);

                Log.d(TAG, "scheduleNext: interval = " + interval + ", time = " + new Date(nextAlarm).toString());
            }
        }
        else {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = createPendingIntent(context);
            alarmManager.cancel(pi);
            Log.d(TAG, "scheduleNext: cancelled");
        }
    }


    private static PendingIntent createPendingIntent(Context context){
        Intent i = new Intent(context, SyncReceiver.class);
        return PendingIntent.getBroadcast(context, RC_SYNC, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
