/*
 * Copyright (C) 2016 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.sample.cast.refplayer.browser;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.sample.cast.refplayer.R;
import com.google.sample.cast.refplayer.utils.CustomVolleyRequest;
import com.google.sample.cast.refplayer.utils.MediaItem;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * An {@link ArrayAdapter} to populate the list of videos.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private static final float mAspectRatio = 9f / 16f;
    private final ItemClickListener mClickListener;
    private final Context mAppContext;
    private List<MediaItem> videos;

    public VideoListAdapter(ItemClickListener clickListener, Context context) {
        mClickListener = clickListener;
        mAppContext = context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.browse_row, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final MediaItem item = videos.get(position);
        viewHolder.setTitle(item.getTitle());
        viewHolder.setDescription(item.getStudio());
        viewHolder.setImage(item.getImage(0), mAppContext);

        viewHolder.mImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.itemClicked(v, item, position);
            }
        });

        viewHolder.mTextContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.itemClicked(v, item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 : videos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View mParent;
        private final View mTextContainer;
        private TextView mTitleView;
        private TextView mDescriptionView;
        private NetworkImageView mImgView;
        private ImageLoader mImageLoader;

        public static ViewHolder newInstance(View parent) {
            NetworkImageView imgView = (NetworkImageView) parent.findViewById(R.id.imageView1);
            TextView titleView = (TextView) parent.findViewById(R.id.textView1);
            TextView descriptionView = (TextView) parent.findViewById(R.id.textView2);
            View textContainer = parent.findViewById(R.id.text_container);
            return new ViewHolder(parent, imgView, textContainer, titleView, descriptionView);
        }

        private ViewHolder(View parent, NetworkImageView imgView, View textContainer, TextView titleView,
                TextView descriptionView) {
            super(parent);
            mParent = parent;
            mImgView = imgView;
            mTextContainer = textContainer;
            mTitleView = titleView;
            mDescriptionView = descriptionView;
        }

        public void setTitle(String title) {
            mTitleView.setText(title);
        }

        public void setDescription(String description) {
            mDescriptionView.setText(description);
        }

        public void setImage(String imgUrl, Context context) {
            mImageLoader = CustomVolleyRequest.getInstance(context)
                    .getImageLoader();
            mImageLoader.get(imgUrl, ImageLoader.getImageListener(mImgView, 0, 0));
            mImgView.setImageUrl(imgUrl, mImageLoader);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mParent.setOnClickListener(listener);
        }

        public ImageView getImageView() {
            return mImgView;
        }
    }

    public void setData(List<MediaItem> data) {
        videos = data;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {

        void itemClicked(View v, MediaItem item, int position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
