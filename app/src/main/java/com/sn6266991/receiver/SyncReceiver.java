package com.sn6266991.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sn6266991.service.FetchNewsService;

/**
 * 6266991
 */
public class SyncReceiver extends BroadcastReceiver {

    public static final String TAG = "SyncReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + intent.toUri(Intent.URI_INTENT_SCHEME));

        Intent i = new Intent(context, FetchNewsService.class);
        context.startService(i);
    }

}
