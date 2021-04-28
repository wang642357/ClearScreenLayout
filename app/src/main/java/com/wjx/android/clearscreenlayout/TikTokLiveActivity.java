package com.wjx.android.clearscreenlayout;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import com.dueeeke.videoplayer.exo.ExoMediaPlayer;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.L;
import com.wjx.android.clearscreenlayout.adapter.TikTokLiveAdapter;
import com.wjx.android.clearscreenlayout.bean.TikTokBean;
import com.wjx.android.clearscreenlayout.control.TikTokController;
import com.wjx.android.clearscreenlayout.control.TikTokView;
import com.wjx.android.clearscreenlayout.render.TikTokRenderViewFactory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 作者：wangjianxiong 创建时间：2021/4/28
 */
public class TikTokLiveActivity extends AppCompatActivity {

    protected VideoView<ExoMediaPlayer> mVideoView;

    private TikTokController mController;

    private TikTokLiveAdapter mTikTokLiveAdapter;

    private RecyclerView mViewPagerImpl;

    private int mCurrentPosition = -1;

    private ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (position == mCurrentPosition) {
                return;
            }
            mViewPager2.post(new Runnable() {
                @Override
                public void run() {
                    startPlay(position);
                }
            });
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    private ViewPager2 mViewPager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        setContentView(R.layout.activity_tik_tok_live);
        mViewPager2 = findViewById(R.id.viewpager);
        mTikTokLiveAdapter = new TikTokLiveAdapter(getUrls());
        mViewPager2.setAdapter(mTikTokLiveAdapter);
        mViewPager2.setOffscreenPageLimit(4);
        mViewPager2.registerOnPageChangeCallback(callback);
        mViewPagerImpl = (RecyclerView) mViewPager2.getChildAt(0);
        initVideoView();
    }

    private void initVideoView() {
        mVideoView = new VideoView<>(this);
        mVideoView.setLooping(true);
        //以下只能二选一，看你的需求
        mVideoView.setRenderViewFactory(TikTokRenderViewFactory.create());
//        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);

        mController = new TikTokController(this);
        mVideoView.setVideoController(mController);
    }


    private void startPlay(int position) {
        int count = mViewPagerImpl.getChildCount();
        for (int i = 0; i < count; i++) {
            View itemView = mViewPagerImpl.getChildAt(i);
            TikTokLiveAdapter.TikTokViewHolder viewHolder
                    = (TikTokLiveAdapter.TikTokViewHolder) itemView.getTag();
            if (viewHolder.getLayoutPosition() == position) {
                mVideoView.release();
                removeViewFormParent(mVideoView);
                TikTokBean tiktokBean = mTikTokLiveAdapter.getItem(position);
                L.i("startPlay: " + "position: " + position + "  url: " + tiktokBean.url);
                mVideoView.setUrl(tiktokBean.url);
                TikTokView tikTokView = viewHolder.getView(R.id.prepareview);
                FrameLayout playContainer = viewHolder.getView(R.id.play_container);
                mController.addControlComponent(tikTokView, true);
                playContainer.addView(mVideoView, 0);
                mVideoView.start();
                mCurrentPosition = position;
                break;
            }
        }
    }

    /**
     * 将View从父控件中移除
     */
    public static void removeViewFormParent(View v) {
        if (v == null) {
            return;
        }
        ViewParent parent = v.getParent();
        if (parent instanceof FrameLayout) {
            ((FrameLayout) parent).removeView(v);
        }
    }


    protected void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                return defaultInsets.replaceSystemWindowInsets(
                        defaultInsets.getSystemWindowInsetLeft(),
                        0,
                        defaultInsets.getSystemWindowInsetRight(),
                        defaultInsets.getSystemWindowInsetBottom());
            });
            ViewCompat.requestApplyInsets(decorView);
            getWindow()
                    .setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    private List<TikTokBean> getUrls() {
        String VIDEO_URL
                = "http://ali.cdn.kaiyanapp.com/1490499356527_f752d403_1280x720.mp4?auth_key=1619601716-0-0-b098d7943a12c2544d42712a8ebf9920";
        String thumbImage
                = "http://img.kaiyanapp.com/dc76f4792449519504649e57e03fbc29.png?imageMogr2/quality/60/format/jpg";
        List<TikTokBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TikTokBean tikTokBean = new TikTokBean();
            tikTokBean.url = VIDEO_URL;
            tikTokBean.thumbImage = thumbImage;
            list.add(tikTokBean);
        }
        return list;
    }

}
