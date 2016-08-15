package com.sn6266991.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sn6266991.entity.Bookmark;

import java.util.ArrayList;

/**
 * This  class is used for performing database operations specially for the intent of this app
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "database.sqlite";

    private static final String CREATE_NEWS_TABLE =
            "CREATE TABLE " + NewsContract.ReadNews.TABLE_NAME + "(" +
                    NewsContract.ReadNews.URL + " TEXT PRIMARY KEY" +
                    ")";

    private static final String CREATE_BOOKMARK_TABLE =
            "CREATE TABLE " + NewsContract.Bookmark.TABLE_NAME + "(" +
                    NewsContract.Bookmark.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NewsContract.Bookmark.TITLE + " TEXT, " +
                    NewsContract.Bookmark.URL + " TEXT UNIQUE, " +
                    NewsContract.Bookmark.CREATED_AT + " INTEGER" +
                    ")";

    private static DatabaseManager instance = null;

    /**
     * Initialize the singleton. Subsequent calls do nothing
     * @param context any {@link Context} belongs to this app
     * @return the {@link DatabaseManager} singleton instance
     */
    public static DatabaseManager initialize(Context context){
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    /**
     * @return The singleton instance
     */
    public static DatabaseManager getInstance(){
        return instance;
    }

    private DatabaseManager(Context context) {
        super(context, NAME, null, VERSION);
    }

    /**
     * This will create 2 tables for storing read URLs and bookmarks
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS_TABLE);
        db.execSQL(CREATE_BOOKMARK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Report that an article URL has been read. This will add the article URL to the database
     * @param url URL of the article
     * @return the id of inserted record
     */
    public long addReadNews(String url){
        ContentValues cv = new ContentValues(1);
        cv.put(NewsContract.ReadNews.URL, url);

        return getWritableDatabase().insertWithOnConflict(NewsContract.ReadNews.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * @return an {@link ArrayList} of all read URLs
     */
    public ArrayList<String> getAllReadUrls(){
        Cursor cursor = getReadableDatabase().query(NewsContract.ReadNews.TABLE_NAME, null, null, null, null, null, null);

        ArrayList<String> readUrls = new ArrayList<>(cursor.getCount());
        if (cursor.moveToFirst()){
            do {
                String url = cursor.getString(cursor.getColumnIndex(NewsContract.ReadNews.URL));
                readUrls.add(url);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return readUrls;
    }

    /**
     * Add bookmark record to the database
     * @param title Article title
     * @param url Article URL
     * @return the ID of inserted record
     */
    public long addBookmark(String title, String url){
        ContentValues cv = new ContentValues(3);
        cv.put(NewsContract.Bookmark.TITLE, title);
        cv.put(NewsContract.Bookmark.URL, url);
        cv.put(NewsContract.Bookmark.CREATED_AT, System.currentTimeMillis());

        return getWritableDatabase().insertWithOnConflict(NewsContract.Bookmark.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * Get all bookmark records
     * @return an {@link ArrayList} of all stored records
     */
    public ArrayList<Bookmark> getBookmarks(){
        Cursor cursor = getReadableDatabase().query(NewsContract.Bookmark.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Bookmark> bookmarks = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(NewsContract.Bookmark.ID);
            int titleIndex = cursor.getColumnIndex(NewsContract.Bookmark.TITLE);
            int urlIndex = cursor.getColumnIndex(NewsContract.Bookmark.URL);
            int createdAtIndex = cursor.getColumnIndex(NewsContract.Bookmark.CREATED_AT);

            do {
                Bookmark bookmark = new Bookmark();
                bookmark.id = cursor.getLong(idIndex);
                bookmark.title = cursor.getString(titleIndex);
                bookmark.url = cursor.getString(urlIndex);
                bookmark.createdAt = cursor.getLong(createdAtIndex);
                bookmarks.add(bookmark);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return bookmarks;
    }

    /**
     * Delete a bookmark
     * @param id the id of the bookmark record to delete
     * @return number of deleted records. Expected values are 0 (indicate that the id does not exist) or 1 (deleted a record)
     */
    public int deleteBookmark(long id){
        return getWritableDatabase().delete(NewsContract.Bookmark.TABLE_NAME, NewsContract.Bookmark.ID + " = " + id, null);
    }

}
