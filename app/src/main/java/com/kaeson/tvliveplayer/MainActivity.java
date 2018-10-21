package com.kaeson.tvliveplayer;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kaeson.ijkplayer.listener.IjkVideoListener;
import com.kaeson.ijkplayer.player.IjkVideoPlayer;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends Activity implements IjkVideoListener{

    private IjkVideoPlayer mVideoPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initItem();
        initVideoPlayer();
    }

    private void initItem() {
        mVideoPlayer=findViewById(R.id.video);
    }

    private void initVideoPlayer() {
        mVideoPlayer.setIjkVideoListener(this);
        mVideoPlayer.setVideoPath("http://ipfs.ztgame.com.cn/QmRRGU4aUZEqJsHxKzBb1ns97GHw45eCRRZFe6Eu8GCmZ4.m3u8");
        try {
            mVideoPlayer.loadVideo();
        } catch (IOException e) {
            Toast.makeText(this,"播放失败",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        mVideoPlayer.stop();
        mVideoPlayer.release();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mVideoPlayer.pause();
        super.onPause();
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        mVideoPlayer.start();
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

    }
}
