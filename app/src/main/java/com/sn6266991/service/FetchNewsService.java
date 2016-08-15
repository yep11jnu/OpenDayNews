package com.sn6266991.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.sn6266991.R;
import com.sn6266991.SyncManager;
import com.sn6266991.Toolbox;
import com.sn6266991.database.DatabaseManager;
import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 6266991
 */
public class FetchNewsService extends IntentService implements Callback {

    public static final String TAG = "FetchNewsService";
    private static final int NOTIFICATION_ID = 156;
    private static final int RC_MAIN_ACTIVITY = 1563;

    /**
     * Creates this service
     */
    public FetchNewsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: fetching news");
        try {
            PkRSS.with(this).load(getString(R.string.url_rss_news_source)).callback(this).tag(TAG).ignoreIfRunning(true).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SyncManager.scheduleNext(this);
    }

    @Override
    public void onPreload() {

    }

    @Override
    public void onLoaded(List<Article> newArticles) {
        Log.d(TAG, "onHandleIntent: " + newArticles.size() + " articles fetched");

        ArrayList<String> readArticles = DatabaseManager.getInstance().getAllReadUrls();

        for (Article article : newArticles){
            String url = article.getSource().toString();
            if (!readArticles.contains(url)){
                Log.d(TAG, "onHandleIntent: has new unread article, notifying now");
                Toolbox.showNotification(this);
                break;
            }
        }
    }

    @Override
    public void onLoadFailed() {

    }
}
