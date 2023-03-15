package com.innovative.shorts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.innovative.shorts.databinding.FragmentHomeBinding;
import com.innovative.shorts.databinding.ItemReelsBinding;

public class HomeFragment extends Fragment implements Player.EventListener {
    FragmentHomeBinding binding;
    SimpleExoPlayer player;
    ItemReelsBinding playerBinding;
    int lastPosition = 0;
    ReelsAdapter reelsAdapter = new ReelsAdapter();

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.rvReels.setAdapter(reelsAdapter);
        reelsAdapter.addData(Democontents.getReels());
        new PagerSnapHelper().attachToRecyclerView(binding.rvReels);
        binding.rvReels.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int position;
                View view;
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0 && (position = ((LinearLayoutManager) HomeFragment.this.binding.rvReels.getLayoutManager()).findFirstCompletelyVisibleItemPosition()) > -1 && HomeFragment.this.lastPosition != position && HomeFragment.this.binding.rvReels.getLayoutManager() != null && (view = HomeFragment.this.binding.rvReels.getLayoutManager().findViewByPosition(position)) != null) {
                    int unused = HomeFragment.this.lastPosition = position;
                    ItemReelsBinding binding1 = (ItemReelsBinding) DataBindingUtil.bind(view);
                    if (binding1 != null) {
                        HomeFragment homeFragment = HomeFragment.this;
                        homeFragment.playVideo(reelsAdapter.getList().get(position).getVideo_url(), binding1);
                        HomeFragment homeFragment2 = HomeFragment.this;
                        //homeFragment2.setThumbnail(reelsAdapter.getList().get(position).getVideo_url(), binding1);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        reelsAdapter.setOnReelsVideoAdapterListner(new ReelsAdapter.OnReelsVideoAdapterListner() {
            @Override
            public void onItemClick(ItemReelsBinding reelsBinding, int pos, int type) {
                if (type == 1) {
                    int unused = HomeFragment.this.lastPosition = pos;
                    HomeFragment homeFragment = HomeFragment.this;
                    homeFragment.playVideo(reelsAdapter.getList().get(pos).getVideo_url(), reelsBinding);
                    HomeFragment homeFragment2 = HomeFragment.this;
                    //homeFragment2.setThumbnail(reelsAdapter.getList().get(pos).getVideo_url(), reelsBinding);
                } else if (HomeFragment.this.player == null) {
                } else {
                    if (HomeFragment.this.player.isPlaying()) {
                        HomeFragment.this.player.setPlayWhenReady(false);
                    } else {
                        HomeFragment.this.player.setPlayWhenReady(true);
                    }
                }
            }

            @Override
            public void onDoubleClick(Video model, MotionEvent event, ItemReelsBinding binding) {

            }

            @Override
            public void onClickLike(ItemReelsBinding reelsBinding, int pos) {

            }

            @Override
            public void onClickUser(Video reel) {

            }

            @Override
            public void onClickComments(Video reels) {

            }

            @Override
            public void onClickShare(Video reel) {

            }

            @Override
            public void onClickGift() {

            }
        });

    }


    /* access modifiers changed from: private */
    public void share(String video) {
        Intent share = new Intent("android.intent.action.SEND");
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra("android.intent.extra.SUBJECT", R.string.app_name);
        share.putExtra("android.intent.extra.TEXT", video);
        startActivity(Intent.createChooser(share, "Share link!"));
    }


    /*public void setThumbnail(String video, ItemReelsBinding reelsBinding) {
        Glide.with(reelsBinding.getRoot()).load(video).apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(new BlurTransformation(25, 5))).into(reelsBinding.thumbnailVideo);
    }*/

    public void playVideo(String videoUrl, ItemReelsBinding binding2) {
        SimpleExoPlayer simpleExoPlayer = this.player;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.removeListener(this);
            this.player.setPlayWhenReady(false);
            this.player.release();
        }
        Log.d("TAG", "playVideo:URL  " + videoUrl);
        this.playerBinding = binding2;
        this.player = new SimpleExoPlayer.Builder(getActivity()).build();
        ProgressiveMediaSource progressiveMediaSource = new ProgressiveMediaSource.Factory(new CacheDataSourceFactory(MainApplication.simpleCache, new DefaultHttpDataSourceFactory(Util.getUserAgent(getActivity(), "TejTok")), CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)).createMediaSource(Uri.parse(videoUrl));
        binding2.playerView.setPlayer(this.player);
        this.player.setPlayWhenReady(true);
        this.player.seekTo(0, 0);
        this.player.setRepeatMode(Player.REPEAT_MODE_ALL);
        this.player.addListener(this);
        binding2.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        this.player.prepare(progressiveMediaSource, true, false);
    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        //new Implemention by reverese
        if (playbackState == 2) {
            if (this.playerBinding != null) {
                this.binding.buffering.setVisibility(View.VISIBLE);
            }
        } else if (playbackState == 3 && this.playerBinding != null) {
            this.binding.buffering.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.release();
        }
        super.onDestroy();
    }

}
