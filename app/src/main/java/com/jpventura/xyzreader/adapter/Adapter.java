/*
 * Copyright 2015 JP Ventura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jpventura.xyzreader.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpventura.xyzreader.R;
import com.jpventura.xyzreader.data.ArticleLoader;
import com.jpventura.xyzreader.ui.OnItemSelectedListener;
import com.jpventura.xyzreader.widget.CursorRecyclerAdapter;

import com.squareup.picasso.Picasso;

public class Adapter extends CursorRecyclerAdapter<Adapter.ViewHolder>
        implements OnClickListener {
    private Context mContext;
    private OnItemSelectedListener<Long> mOnItemSelectedListener;

    public Adapter(Context context, Cursor cursor) {
        super(cursor);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_article, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor data) {
        holder.itemView.setTag(Long.valueOf(data.getLong(ArticleLoader.Query._ID)));
        holder.titleView.setText(data.getString(ArticleLoader.Query.TITLE));
        holder.subtitleView.setText(data.getString(ArticleLoader.Query.AUTHOR));
        Picasso.with(mContext)
                .load(data.getString(ArticleLoader.Query.THUMB_URL))
                .into(holder.thumbnailView, new GetThumbnailPaletteCommand(holder.itemView));
    }

    @Override
    public void onClick(View view) {
        if (null != mOnItemSelectedListener) {
            mOnItemSelectedListener.onItemSelected(view, (Long) view.getTag());
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<Long> onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        }
    }
}
