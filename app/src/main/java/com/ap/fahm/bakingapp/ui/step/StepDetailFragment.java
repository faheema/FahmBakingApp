/*
 * Copyright 2017.  FahmApps.com
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

package com.ap.fahm.bakingapp.ui.step;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ap.fahm.bakingapp.R;
import com.ap.fahm.bakingapp.model.pojo.Step;
import com.ap.fahm.bakingapp.util.DisplayMetricUtils;
import com.ap.fahm.bakingapp.util.GlideApp;
import com.ap.fahm.bakingapp.util.ImageUtils;
import com.ap.fahm.bakingapp.util.TextUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    private static MediaSessionCompat mMediaSession;
    @BindView(R.id.tv_step_short_description)
    TextView tvStepShortDescription;
    @BindView(R.id.tv_step_description)
    TextView tvStepDescription;
    @BindView(R.id.video_player_view)
    SimpleExoPlayerView videoPlayerView;
    @BindView(R.id.pb_buffering)
    ProgressBar pbBuffering;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private Step step;
    private boolean isTablet;
    private SimpleExoPlayer mExoPlayer;
    private PlaybackStateCompat.Builder mStateBuilder;
    private String videoUrl;
    private long currentVideoPosition;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(Step step, boolean isTablet) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("step", step);
        bundle.putBoolean("isTablet", isTablet);
        stepDetailFragment.setArguments(bundle);

        return stepDetailFragment;
    }

    public Step getCurrentStep() {
        return step;
    }

    public void setOrientation(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams layoutParams =
                    videoPlayerView.getLayoutParams();
            layoutParams.width = DisplayMetricUtils.getDeviceWidth(getActivity());
            layoutParams.height = DisplayMetricUtils.getDeviceHeight(getActivity());
            videoPlayerView.setLayoutParams(layoutParams);

            tvStepDescription.setVisibility(View.GONE);
            tvStepShortDescription.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams layoutParams =
                    videoPlayerView.getLayoutParams();
            layoutParams.width = DisplayMetricUtils.getDeviceWidth(getActivity());
            layoutParams.height = (int) (9.0f / 16.0f * layoutParams.width);
            videoPlayerView.setLayoutParams(layoutParams);

            tvStepDescription.setVisibility(View.VISIBLE);
            tvStepShortDescription.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("OnCreateView", "TRUE");
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            step = getArguments().getParcelable("step");
            isTablet = getArguments().getBoolean("isTablet");
            if (step != null) {
                tvStepShortDescription.setText(step.getShortDescription());
                tvStepDescription.setText(step.getDescription());
                if ((step.getVideoURL() == null || step.getVideoURL().isEmpty()) &&
                        (step.getThumbnailURL() == null || step.getThumbnailURL().isEmpty())) {
                    videoPlayerView.setVisibility(View.GONE);
                } else {
                    if (isTablet) {
                        ViewGroup.LayoutParams layoutParams =
                                videoPlayerView.getLayoutParams();
                        layoutParams.width = (int) (2.0f / 3.0f *
                                DisplayMetricUtils.getDeviceWidth(getActivity()));
                        layoutParams.height = (int) (9.0f / 16.0f * layoutParams.width);
                        videoPlayerView.setLayoutParams(layoutParams);
                    }

                    if (step.getThumbnailURL() != null && step.getThumbnailURL().length() >= 3 &&
                            !TextUtils.getExtension(step.getThumbnailURL()).equalsIgnoreCase("mp4")) {

                        GlideApp.with(this)
                                .load(step.getThumbnailURL())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e,
                                                                Object model, Target<Drawable> target,
                                                                boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model,
                                                                   Target<Drawable> target,
                                                                   DataSource dataSource,
                                                                   boolean isFirstResource) {
                                        videoPlayerView.setDefaultArtwork(
                                                ImageUtils.drawableToBitmap(resource));
                                        return true;
                                    }
                                });
                    }

                    if (step.getVideoURL() != null && !step.getVideoURL().equalsIgnoreCase("")) {
                        videoUrl = step.getVideoURL();
                        initializePlayer();
                    } else {
                        videoPlayerView.setVisibility(View.GONE);
                    }
                }
            }
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
        currentVideoPosition = mExoPlayer.getCurrentPosition();
        }catch (NullPointerException e)
        {e.printStackTrace();}
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    private void initializePlayer() {
        initializeMediaSession();
        initializeVideoPlayer();
    }

    private void initializeMediaSession() {
        if (mMediaSession == null) {
            mMediaSession = new MediaSessionCompat(getActivity(), "Recipe");
            mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mMediaSession.setMediaButtonReceiver(null);

            mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                    PlaybackStateCompat.ACTION_PLAY |
                            PlaybackStateCompat.ACTION_PAUSE |
                            PlaybackStateCompat.ACTION_PLAY_PAUSE);

            mMediaSession.setPlaybackState(mStateBuilder.build());

            mMediaSession.setCallback(new MediaSessionCompat.Callback() {
                @Override
                public void onPlay() {
                    mExoPlayer.setPlayWhenReady(true);
                }

                @Override
                public void onPause() {
                    mExoPlayer.setPlayWhenReady(false);
                }

                @Override
                public void onSkipToPrevious() {
                    mExoPlayer.seekTo(0);
                }
            });

            mMediaSession.setActive(true);
        }
    }

    private void initializeVideoPlayer() {
        if (mExoPlayer == null && videoUrl != null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mExoPlayer.seekTo(currentVideoPosition);
            videoPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getActivity(), "Recipe");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl), new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        try {
            if (mExoPlayer != null) {
                mExoPlayer.stop();
                mExoPlayer.release();
                mExoPlayer = null;
            }
            if (mMediaSession != null) {
                mMediaSession.setActive(false);
            }
        }catch (NullPointerException e)
        { e.printStackTrace();}
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        try {
            if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {

                //   mStateBuilder = is null
                mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        mExoPlayer.getCurrentPosition(), 1f);
                pbBuffering.setVisibility(View.GONE);
            } else if (playbackState == ExoPlayer.STATE_READY) {
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        mExoPlayer.getCurrentPosition(), 1f);
                pbBuffering.setVisibility(View.GONE);
            } else if (playbackState == ExoPlayer.STATE_BUFFERING) {
                pbBuffering.setVisibility(View.VISIBLE);
            } else {
                pbBuffering.setVisibility(View.GONE);
            }
            if (mStateBuilder != null) {
                mMediaSession.setPlaybackState(mStateBuilder.build());
            }
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        showError(getString(R.string.label_no_internet));
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    public void showError(String message) {
        Snackbar snackbar = Snackbar
                .make(llContainer, message, Snackbar.LENGTH_LONG);

        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.red_700));

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red_100));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.red_700));
        snackbar.show();
    }
}
