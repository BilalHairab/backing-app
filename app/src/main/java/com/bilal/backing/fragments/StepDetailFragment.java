package com.bilal.backing.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bilal.backing.R;
import com.bilal.backing.activities.StepDetailActivity;
import com.bilal.backing.interfaces.OnStepChanged;
import com.bilal.backing.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {
    private static final String ARG_STEP = "step";
    private static final String ARG_LAST_STEP = "last";
    //    private static final String ARG_STEP_CHANGED = "changed";
    private static final String ARG_PLAY_READY = "ready";
    private static final String ARG_CURRENT_POSITION = "position";

    private OnStepChanged onStepChanged;
    SimpleExoPlayer player;
    private Step step;
    private boolean isLastStep;
    Context context;
    long playbackPosition = 0;
    int currentWindow = 0;
    boolean playWhenReady = true;
    View layout;
    private boolean isTablet;
    @BindView(R.id.tv_step_description)
    TextView tvDescription;

    @BindView(R.id.btn_next)
    Button button;

    @BindView(R.id.exo_video)
    SimpleExoPlayerView playerView;

    private boolean mExoPlayerFullscreen = false;
    private Dialog mFullScreenDialog;

    public StepDetailFragment() {

    }

    public static StepDetailFragment newInstance(Step step, boolean isLastStep) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);
        args.putBoolean(ARG_LAST_STEP, isLastStep);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_STEP)) {
            step = savedInstanceState.getParcelable(ARG_STEP);
            playbackPosition = savedInstanceState.getLong(ARG_CURRENT_POSITION);
            playWhenReady = savedInstanceState.getBoolean(ARG_PLAY_READY);
            isLastStep = savedInstanceState.getBoolean(ARG_LAST_STEP);
        } else if (getArguments() != null) {
            step = getArguments().getParcelable(ARG_STEP);
            isLastStep = getArguments().getBoolean(ARG_LAST_STEP);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_detail, container, false);
    }

    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        layout = view;
        context = view.getContext();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onStepChanged = (OnStepChanged) context;
            isTablet = !(context instanceof StepDetailActivity);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MyInterface ");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (step != null) {
            outState.putParcelable(ARG_STEP, step);
            outState.putBoolean(ARG_LAST_STEP, isLastStep);
            if (player != null) {
                outState.putLong(ARG_CURRENT_POSITION, player.getCurrentPosition());
                outState.putBoolean(ARG_PLAY_READY, player.getPlayWhenReady());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStepChanged.onNextStep();
            }
        });
        if (isLastStep)
            button.setVisibility(View.GONE);
        tvDescription.setText(step.getDescription());

    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
        initFullscreenDialog();
        if (!isTablet) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                openFullscreenDialog();
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                closeFullscreenDialog();
            }
        }
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(context),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        Uri uri = Uri.parse(step.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void openFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) layout.findViewById(R.id.main_media_frame)).addView(playerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
    }
}
