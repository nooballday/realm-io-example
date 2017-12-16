package com.personal.naufal.newsapiexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * Created by Naufal on 16/12/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<NewsModel> modelList = new ArrayList<>();
    private Context _context;

    public NewsAdapter(List<NewsModel> modelList, Context _context) {
        this.modelList = modelList;
        this._context = _context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(_context).inflate(R.layout.row_news, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        NewsModel currentData = modelList.get(position);
        holder.mNewsAuthor.setText(currentData.getAuthor());
        holder.mNewsTitle.setText(currentData.getTitle());
        holder.mNewsDesc.setText(currentData.getDesc());

        final Picasso picasso =  new Picasso.Builder(_context)
                .downloader(new OkHttp3Downloader(okClient()))
                .build();
        picasso
                .load(currentData.getUrlToImage())
                .error(R.drawable.no_image_placeholder)
                .placeholder(R.drawable.no_image_placeholder)
                .into(holder.mNewsImage);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_image)
        ImageView mNewsImage;
        @BindView(R.id.news_author)
        TextView mNewsAuthor;
        @BindView(R.id.news_desc)
        TextView mNewsDesc;
        @BindView(R.id.news_title)
        TextView mNewsTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OkHttpClient okClient() {
       return new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();
    }
}
