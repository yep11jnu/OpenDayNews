package com.sn6266991.view.adapter;

/**
 * Simple interface for click callback
 */
public interface ClickListener<T> {
    void onClick(int viewId, int position, T object);
}
