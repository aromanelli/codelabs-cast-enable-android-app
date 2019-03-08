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

import com.google.sample.cast.refplayer.R;
import com.google.sample.cast.refplayer.mediaplayer.LocalPlayerActivity;
import com.google.sample.cast.refplayer.utils.MediaItem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * A fragment to host a list view of the video catalog.
 */
public class VideoBrowserFragment
        extends Fragment
        implements
            VideoListAdapter.ItemClickListener,
            LoaderManager.LoaderCallbacks<List<MediaItem>> {

    private static final String TAG = "VideoBrowserFragment";
    private RecyclerView mRecyclerView;
    private VideoListAdapter mAdapter;
    private View mEmptyView;
    private View mLoadingView;

    public VideoBrowserFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_browser_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getView().findViewById(R.id.list);
        mEmptyView = getView().findViewById(R.id.empty_view);
        mLoadingView = getView().findViewById(R.id.progress_indicator);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new VideoListAdapter(this, this.getContext());
        mRecyclerView.setAdapter(mAdapter);
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    @Override
    public void itemClicked(View view, MediaItem item, int position) {
        String transitionName = getString(R.string.transition_image);
        VideoListAdapter.ViewHolder viewHolder =
                (VideoListAdapter.ViewHolder) mRecyclerView.findViewHolderForPosition(position);
        Pair<View, String> imagePair =
                Pair.create((View) viewHolder.getImageView(), transitionName);

        @SuppressWarnings("unchecked")
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this.getActivity(), imagePair);

        Intent intent = new Intent(getActivity(), LocalPlayerActivity.class);
        intent.putExtra("media", item.toBundle());
        intent.putExtra("shouldStart", false);
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }

    @Override
    @NonNull
    public Loader<List<MediaItem>> onCreateLoader(int id, Bundle args) {
        return new VideoItemLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MediaItem>> loader, List<MediaItem> data) {
        mAdapter.setData(data);
        mLoadingView.setVisibility(View.GONE);
        mEmptyView.setVisibility(null == data || data.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MediaItem>> loader) {
        mAdapter.setData(null);
    }

}
