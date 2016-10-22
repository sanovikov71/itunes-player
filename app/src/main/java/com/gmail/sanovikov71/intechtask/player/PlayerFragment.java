package com.gmail.sanovikov71.intechtask.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.gmail.sanovikov71.intechtask.R;

import java.lang.ref.WeakReference;
import java.util.Random;

public class PlayerFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private SeekBar mSeekBar;
    private Button mButton;

    private UpdatesHandler mHandler;

    public static PlayerFragment newInstance() {
        Bundle args = new Bundle();

        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        mHandler =  new UpdatesHandler(this);

        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);

        mButton = (Button) view.findViewById(R.id.button);
        mButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (noMusic()) {
            play();
            startSeekBarUpdates();
            mButton.setText(getText(R.string.pause));
        } else {
            pause();
            stopSeekBarUpdates();
            mButton.setText(getText(R.string.start));
        }
    }

    private void startSeekBarUpdates() {
        mHandler.sendEmptyMessage(UpdatesHandler.SEEKBAR_UPDATE);
    }


    private void stopSeekBarUpdates() {
        mHandler.removeMessages(UpdatesHandler.SEEKBAR_UPDATE);
    }

    private boolean noMusic() {
        return mButton.getText().equals(getText(R.string.start));
    }

    private void pause() {

    }

    private void play() {

    }

    private void setSongProgress(int progress) {
        mSeekBar.setProgress(progress);
    }

    private static class UpdatesHandler extends Handler {

        static final int SEEKBAR_UPDATE = 100;
        static final int DELAY_MILLIS = 1000;

        private final WeakReference<PlayerFragment> mFragment;

        public UpdatesHandler(PlayerFragment fragment) {
            mFragment = new WeakReference<PlayerFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            Random r = new Random();
            PlayerFragment fragment = mFragment.get();
            if (fragment != null) {
                if (msg.what == SEEKBAR_UPDATE) {
                    sendEmptyMessageDelayed(SEEKBAR_UPDATE, DELAY_MILLIS);
                    fragment.setSongProgress(r.nextInt(100));
                }
            }
        }
    }

}

