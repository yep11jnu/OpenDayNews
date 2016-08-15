package com.sn6266991.view.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sn6266991.R;
import com.sn6266991.entity.NewsItem;

import java.util.ArrayList;

/**
 * Created by k15bh on 06/08/2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    ArrayList<NewsItem> newsItems;
    ClickListener<NewsItem> clickListener;
    @LayoutRes int layoutResId;

    public NewsAdapter(ArrayList<NewsItem> newsItems, @LayoutRes int layoutResId){
        this.newsItems = newsItems;
        this.layoutResId = layoutResId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        final ViewHolder vh = new ViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null){
                    int position = vh.getLayoutPosition();
                    clickListener.onClick(itemView.getId(), position, newsItems.get(position));
                }
            }
        });

        vh.bookmarkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null){
                    int position = vh.getLayoutPosition();
                    clickListener.onClick(vh.bookmarkImageView.getId(), position, newsItems.get(position));
                }
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.populateWithItem(newsItems.get(position));
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public void setClickListener(ClickListener<NewsItem> clickListener) {
        this.clickListener = clickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView bookmarkImageView;

        ViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.text_article_title);
            bookmarkImageView = (ImageView) itemView.findViewById(R.id.img_bookmark);
        }


        void populateWithItem(NewsItem newsItem){
            titleTextView.setText(newsItem.article.getTitle());

            titleTextView.setTextColor(newsItem.isRead? Color.GRAY : Color.BLACK);
            //bookmarkImageView.setVisibility(newsItem.bookmarked? View.VISIBLE : View.GONE);
        }

    }


}
