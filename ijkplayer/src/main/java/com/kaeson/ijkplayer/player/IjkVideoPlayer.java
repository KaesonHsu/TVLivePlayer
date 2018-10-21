package com.kaeson.ijkplayer.player;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kaeson.ijkplayer.listener.IjkVideoListener;

import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Kaeson.Xu on 2018/10/19.
 *
 */

public class IjkVideoPlayer extends FrameLayout {


    private Context mContext;
    private SurfaceView mSurfaceView;
    private IMediaPlayer mImediaPlayer = null;

    private IjkVideoListener mIjkVideoListener;

    private boolean mEnableMediaCodec;

    private String mVideoPath;
    private Map<String, String> mHeader;
    private AudioManager audioManager;

    public IjkVideoPlayer(@NonNull Context context) {
        this(context, null);
    }

    public IjkVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IjkVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public IjkVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setBackgroundColor(Color.BLACK);
        createSurfaceView();

        audioManager = (AudioManager) mContext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    private void createSurfaceView() {
        mSurfaceView = new SurfaceView(mContext);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                if (mImediaPlayer != null) {
                    mImediaPlayer.setDisplay(surfaceHolder);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                , Gravity.CENTER);
        addView(mSurfaceView, 0, layoutParams);
    }

    public void setVideoPath(String mVideoPath) {
        setVideoPath(mVideoPath,null);
    }

    public void setVideoPath(String mVideoPath, Map<String, String> mHeader) {
        this.mVideoPath = mVideoPath;
        this.mHeader = mHeader;
    }

    private IMediaPlayer createIjkPlayer() {
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "http-detect-range-support", 1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "min-frames", 100);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);

        ijkMediaPlayer.setVolume(1.0f,1.0f);
        setEnableMediaCodec(ijkMediaPlayer,mEnableMediaCodec);

        return ijkMediaPlayer;
    }

    private void setEnableMediaCodec(IjkMediaPlayer ijkMediaPlayer, boolean isEnable) {
        int value = isEnable ? 1 : 0;
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", value);//开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", value);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", value);
    }
    public void setIjkVideoListener(IjkVideoListener ijkVideoListener) {
        mIjkVideoListener = ijkVideoListener;
    }


    private void setIjkPlayerListener(IMediaPlayer iMediaPlayer) {
        iMediaPlayer.setOnPreparedListener(mPreparedListener);
        iMediaPlayer.setOnVideoSizeChangedListener(mVideoSizeChangedListener);
    }

    private IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            if (mIjkVideoListener != null) {
                mIjkVideoListener.onPrepared(iMediaPlayer);
            }
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
            int videoWidth = iMediaPlayer.getVideoWidth();
            int videoHight = iMediaPlayer.getVideoHeight();
            if (videoWidth != 0 && videoHight != 0) {
                mSurfaceView.getHolder().setFixedSize(videoWidth, videoHight);
            }
        }
    };

    public void loadVideo() throws IOException {
        if (mImediaPlayer != null) {
            mImediaPlayer.stop();
            mImediaPlayer.release();
        }
        mImediaPlayer = createIjkPlayer();
        setIjkPlayerListener(mImediaPlayer);
        mImediaPlayer.setDisplay(mSurfaceView.getHolder());
        mImediaPlayer.setDataSource(mContext, Uri.parse(mVideoPath), mHeader);

        mImediaPlayer.prepareAsync();
    }

    public void start() {
        if (mImediaPlayer != null) {
            mImediaPlayer.start();
        }
    }

    public void release() {
        if (mImediaPlayer != null) {
            mImediaPlayer.reset();
            mImediaPlayer.release();
            mImediaPlayer = null;
        }
    }

    public void pause() {
        if (mImediaPlayer != null) {
            mImediaPlayer.pause();
        }
    }

    public void stop() {
        if (mImediaPlayer != null) {
            mImediaPlayer.stop();
        }
    }


    public void reset() {
        if (mImediaPlayer != null) {
            mImediaPlayer.reset();
        }
    }


    public long getDuration() {
        if (mImediaPlayer != null) {
            return mImediaPlayer.getDuration();
        } else {
            return 0;
        }
    }


    public long getCurrentPosition() {
        if (mImediaPlayer != null) {
            return mImediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }


    public void seekTo(long l) {
        if (mImediaPlayer != null) {
            mImediaPlayer.seekTo(l);
        }
    }

    public boolean isPlaying(){
        if(mImediaPlayer != null) {
            return mImediaPlayer.isPlaying();
        }
        return false;
    }
}
