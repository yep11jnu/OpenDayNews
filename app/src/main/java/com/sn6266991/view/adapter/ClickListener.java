package com.sn6266991.view.adapter;

/**
 * Simple interface for click callback
 * 6266991
 */
public interface ClickListener<T> {
    void onClick(int viewId, int position, T object);
}
