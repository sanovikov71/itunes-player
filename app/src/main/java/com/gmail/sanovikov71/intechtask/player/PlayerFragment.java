package com.gmail.sanovikov71.intechtask.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gmail.sanovikov71.intechtask.R;
import com.gmail.sanovikov71.intechtask.network.Song;

import java.lang.ref.WeakReference;

public class PlayerFragment extends Fragment implements View.OnClickListener, UISetter {

    private static final String SONG_OBJECT = "song_object";
    private static final int INVALID_VALUE = -1;

    private MusicHelper mMusicHelper;
    private SeekBar mSeekBar;
    private Button mButton;

    private UpdatesHandler mHandler;

    private int mSongDuration = INVALID_VALUE;
    private boolean mPlayerPrepared;

    public static PlayerFragment newInstance(Parcelable song) {
        Bundle args = new Bundle();
        args.putParcelable(SONG_OBJECT, song);

        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        Song song = getArguments().getParcelable(SONG_OBJECT);
        if (song != null) {
            ImageView image = (ImageView) view.findViewById(R.id.image);
            Glide.with(this).load(song.getArtworkUrl100()).into(image);
            TextView name = (TextView) view.findViewById(R.id.name);
            String fullName = song.getArtistName() + " - " + song.getTrackName();
            name.setText(fullName);
        }

        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);
        // disables clicks
        mSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        if (mSongDuration != INVALID_VALUE) {
            mSeekBar.setMax(mSongDuration);
        }

        mButton = (Button) view.findViewById(R.id.button);
        mButton.setOnClickListener(this);

        mSeekBar.setProgress(mMusicHelper.getCurrentPosition());
        if (mMusicHelper.isPlaying()) {
            mButton.setText(getText(R.string.pause));
        } else {
            mButton.setText(getText(R.string.start));
        }

        mHandler = new UpdatesHandler(mSeekBar, mMusicHelper);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (!mMusicHelper.isPlaying()) {
            if (mPlayerPrepared) {
                mMusicHelper.play();
                startSeekBarUpdates();
                mButton.setText(getText(R.string.pause));
            }
        } else {
            mMusicHelper.pause();
            stopSeekBarUpdates();
            mButton.setText(getText(R.string.start));
        }
    }

    private void startSeekBarUpdates() {
        Message message =
                mHandler.obtainMessage(UpdatesHandler.SEEKBAR_UPDATE, mMusicHelper.getCurrentPosition(), 0);
        mHandler.sendMessage(message);
    }

    private void stopSeekBarUpdates() {
        mHandler.removeMessages(UpdatesHandler.SEEKBAR_UPDATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMusicHelper.isPlaying()) {
            startSeekBarUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopSeekBarUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMusicHelper.releaseMediaPlayer();
    }

    @Override
    public void playerStateObtained(int duration) {
        mPlayerPrepared = true;
        mSongDuration = duration;
        mSeekBar.setMax(mSongDuration);
    }

    @Override
    public void songEnded() {
        stopSeekBarUpdates();
    }

    public void setMusicHelper(MusicHelper musicHelper) {
        mMusicHelper = musicHelper;
    }

    private static class UpdatesHandler extends Handler {

        static final int SEEKBAR_UPDATE = 100;
        static final int DELAY_MILLIS = 1000;

        private final WeakReference<SeekBar> mSeekBar;
        private final WeakReference<MusicHelper> mMusicHelper;

        public UpdatesHandler(SeekBar seekBar, MusicHelper musicHelper) {
            mSeekBar = new WeakReference<>(seekBar);
            mMusicHelper = new WeakReference<>(musicHelper);
        }

        @Override
        public void handleMessage(Message msg) {
            SeekBar seekBar = mSeekBar.get();
            MusicHelper musicHelper = mMusicHelper.get();
            if (seekBar != null && musicHelper != null) {
                if (msg.what == SEEKBAR_UPDATE) {
                    seekBar.setProgress(msg.arg1);
                    Message newMessage =
                            obtainMessage(UpdatesHandler.SEEKBAR_UPDATE, musicHelper.getCurrentPosition(), 0);
                    sendMessageDelayed(newMessage, DELAY_MILLIS);
                }
            }
        }
    }

}

