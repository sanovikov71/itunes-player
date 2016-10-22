package com.gmail.sanovikov71.intechtask.player;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class MusicHelper implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;

    private UISetter mUISetter;

    private int mProgress = 0;

    void prepareMediaPlayer(String dataSource, UISetter uiSetter) {
        mUISetter = uiSetter;

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(dataSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mUISetter.playerStateObtained(mp.getDuration());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mUISetter.songEnded();
    }

    void play() {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(mProgress);
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                System.out.println("Start here lol");
            }
        }
    }

    void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mProgress = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
        }
    }

    void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public void seekTo(int progress) {
        mMediaPlayer.seekTo(progress);
    }
}
