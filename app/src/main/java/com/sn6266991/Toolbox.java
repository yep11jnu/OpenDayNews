package com.sn6266991;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.sn6266991.activity.MainActivity;

/**
 * 6266991
 */
public class Toolbox {

    private static final int NOTIFICATION_ID = 156;
    private static final int RC_MAIN_ACTIVITY = 1563;

    /**
     * Quick helper method to open an url
     * @param target either {@link Uri} or {@link String} contains the link
     */
    public static void openUrl(Context context, Object target){
        Uri uri;
        if (target instanceof String){
            uri = Uri.parse((String)target);
        }
        else if (target instanceof Uri){
            uri = (Uri) target;
        }
        else {
            throw new IllegalArgumentException("target must be either String or android.net.Uri");
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(uri);
        context.startActivity(i);
    }

    /**
     * Simple method to show the notification telling user that there are new unread articles
     */
    public static void showNotification(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean soundEnabled = preferences.getBoolean(context.getString(R.string.preference_key_notification_sound), true);
        boolean vibrateEnabled = preferences.getBoolean(context.getString(R.string.preference_key_notification_vibrate), true);

        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, RC_MAIN_ACTIVITY, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_message))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setLights(Color.WHITE, 750, 750)
                .setContentIntent(pendingIntent);


        if (soundEnabled){
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(soundUri);
        }

        if (vibrateEnabled){
            builder.setVibrate(new long[]{ 500, 500, 300, 500 });
        }

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }

}
