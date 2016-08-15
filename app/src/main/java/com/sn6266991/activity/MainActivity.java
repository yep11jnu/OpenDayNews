package com.sn6266991.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sn6266991.R;
import com.sn6266991.Toolbox;
import com.sn6266991.database.DatabaseManager;
import com.sn6266991.entity.NewsItem;
import com.sn6266991.view.adapter.ClickListener;
import com.sn6266991.view.adapter.NewsAdapter;
import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements Callback {

    RecyclerView articlesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articlesRecycler = (RecyclerView) findViewById(R.id.recycler_articles);
        applyRecyclerLayout(false);

        // Load the RSS feed
        PkRSS.with(this).load(getString(R.string.url_rss_news_source)).callback(this).async();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_bookmark: {
                // Clicked "Bookmarks"
                Intent i = new Intent(this, BookmarksActivity.class);
                startActivity(i);
                return true;
            }

            case R.id.menu_settings: {
                // Clicked "Settings"
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        // If the layout preference is changed, apply the new layout, and recreate the activity
        if (key.equals(getString(R.string.preference_key_layout))){
            applyRecyclerLayout(true);
        }
    }

    /**
     * Refresh the layout
     * @param needRecreate whether activity recreation is needed
     */
    void applyRecyclerLayout(boolean needRecreate){
        String layoutKey = getString(R.string.preference_key_layout);
        String layoutValueList = getString(R.string.preference_value_list_layout);
        String layoutValueGrid = getString(R.string.preference_value_grid_layout);

        String layoutValue = sharedPreferences.getString(layoutKey, layoutValueList);
        if (layoutValue.equals(layoutValueList)){
            articlesRecycler.setLayoutManager(new LinearLayoutManager(this));
        }
        else if (layoutValue.equals(layoutValueGrid)){
            articlesRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        }

        if (needRecreate){
            recreate();
        }
    }

    @Override
    public void onPreload() {

    }

    // Will be called when the RSS loading and parsing are done
    @Override
    public void onLoaded(List<Article> newArticles) {
        // Create array of NewsItems objects
        ArrayList<NewsItem> newsItems = new ArrayList<>(newArticles.size());
        for (int i = 0; i < newArticles.size(); i++){
            Article article = newArticles.get(i);
            NewsItem newsItem = new NewsItem();
            newsItem.article = article;
            newsItems.add(newsItem);
        }

        // Mark read
        ArrayList<String> readUrls = DatabaseManager.getInstance().getAllReadUrls();
        for (NewsItem newsItem : newsItems){
            String url = newsItem.article.getSource().toString();
            if (readUrls.contains(url)){
                newsItem.isRead = true;
            }
        }

        // Load the layout preferences
        String layoutKey = getString(R.string.preference_key_layout);
        String layoutValueList = getString(R.string.preference_value_list_layout);
        String layoutValueGrid = getString(R.string.preference_value_grid_layout);

        @LayoutRes int layoutResId;

        String layoutValue = sharedPreferences.getString(layoutKey, layoutValueList);
        if (layoutValue.equals(layoutValueList)){
            layoutResId = R.layout.adapter_news_item_1;
        }
        else {
            layoutResId = R.layout.adapter_news_item_2;
        }

        final NewsAdapter adapter = new NewsAdapter(newsItems, layoutResId);

        // Click listener
        adapter.setClickListener(new ClickListener<NewsItem>() {
            @Override
            public void onClick(int viewId, int position, NewsItem object) {
                switch (viewId){
                    case R.id.img_bookmark:
                        DatabaseManager.getInstance().addBookmark(object.article.getTitle(), object.article.getSource().toString());
                        Toast.makeText(MainActivity.this, "Added to Bookmarks", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        // Open the url
                        Uri uri = object.article.getSource();
                        openWebPage(uri);

                        // Mark as read and refresh the list
                        object.isRead = true;
                        adapter.notifyItemChanged(position);

                        break;
                }

            }
        });

        // Populate to views
        articlesRecycler.setAdapter(adapter);
    }

    @Override
    public void onLoadFailed() {

    }

    /**
     * Perform opening web page and mark it as read
     */
    void openWebPage(Uri uri){
        Toolbox.openUrl(this, uri);
        String url = uri.toString();
        DatabaseManager.getInstance().addReadNews(url);
    }

}
