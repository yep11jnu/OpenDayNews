package com.sn6266991.entity;

import android.support.annotation.NonNull;

/**
 * 6266991
 */
public class Bookmark implements Comparable<Bookmark>{

    public long id;
    public String title;
    public String url;
    public long createdAt;

    @Override
    public int compareTo(@NonNull Bookmark another) {
        return (int) (another.createdAt - createdAt);
    }
}
