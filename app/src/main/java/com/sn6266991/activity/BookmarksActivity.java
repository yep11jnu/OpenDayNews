package com.sn6266991.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sn6266991.R;
import com.sn6266991.Toolbox;
import com.sn6266991.database.DatabaseManager;
import com.sn6266991.entity.Bookmark;
import com.sn6266991.view.adapter.BookmarkAdapter;
import com.sn6266991.view.adapter.ClickListener;

import java.util.ArrayList;

/**
 * Activity for showing saved bookmarks, which are retrieved from database
 */
public class BookmarksActivity extends BaseActivity {

    RecyclerView articlesRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        articlesRecycler = (RecyclerView) findViewById(R.id.recycler_articles);
        applyRecyclerLayout(false);
        loadBookmarks();
    }

    /**
     * Load bookmarks from database and populate them into the views
     */
    void loadBookmarks(){
        // Load the layout preference
        String layoutKey = getString(R.string.preference_key_layout);
        String layoutValueList = getString(R.string.preference_value_list_layout);
        String layoutValueGrid = getString(R.string.preference_value_grid_layout);

        @LayoutRes int layoutResId;

        String layoutValue = sharedPreferences.getString(layoutKey, layoutValueList);
        if (layoutValue.equals(layoutValueList)){
            layoutResId = R.layout.adapter_bookmark_1;
        }
        else {
            layoutResId = R.layout.adapter_bookmark_2;
        }

        // Load bookmarks
        final ArrayList<Bookmark> bookmarks = DatabaseManager.getInstance().getBookmarks();

        final BookmarkAdapter adapter = new BookmarkAdapter(bookmarks, layoutResId);

        // Click listener
        adapter.setClickListener(new ClickListener<Bookmark>() {
            @Override
            public void onClick(int viewId, int position, Bookmark object) {
                switch (viewId){
                    case R.id.btn_delete:
                        DatabaseManager.getInstance().deleteBookmark(object.id);
                        bookmarks.remove(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(BookmarksActivity.this, R.string.toast_bookmark_deleted, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        String url = object.url;
                        Toolbox.openUrl(BookmarksActivity.this, url);
                        break;
                }
            }
        });

        // Populate into views
        articlesRecycler.setAdapter(adapter);
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

}
