package com.sn6266991.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sn6266991.R;
import com.sn6266991.entity.Bookmark;

import java.util.ArrayList;

/**
 * Adapter for populating {@link Bookmark} items into a {@link RecyclerView}
 * 6266991
 */
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    ArrayList<Bookmark> bookmarks;
    ClickListener<Bookmark> clickListener;
    @LayoutRes int layoutResId;

    /**
     * Create this adapter
     * @param bookmarks collection of bookmarks
     * @param layoutResId layout resource
     */
    public BookmarkAdapter(ArrayList<Bookmark> bookmarks, @LayoutRes int layoutResId) {
        this.bookmarks = bookmarks;
        this.layoutResId = layoutResId;
    }

    /**
     * Creates view holder. Also set up click events
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        final ViewHolder vh = new ViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null){
                    int position = vh.getLayoutPosition();
                    clickListener.onClick(itemView.getId(), position, bookmarks.get(position));
                }
            }
        });

        vh.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null){
                    int position = vh.getLayoutPosition();
                    clickListener.onClick(vh.deleteBtn.getId(), position, bookmarks.get(position));
                }
            }
        });

        return vh;
    }

    /**
     * Populate data to the views belong to that view holder
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.populateViewsWith(bookmarks.get(position));
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    public void setClickListener(ClickListener<Bookmark> clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * View holder contains references to the the views it contains
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        View deleteBtn;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.text_article_title);
            deleteBtn = itemView.findViewById(R.id.btn_delete);
        }

        void populateViewsWith(Bookmark bookmark){
            titleTextView.setText(bookmark.title);
        }

    }

}
