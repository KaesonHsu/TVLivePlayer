package com.kaeson.ijkplayer.listener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Kaeson.Xu on 2018/10/19.
 */

public interface IjkVideoListener extends IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener{
}
