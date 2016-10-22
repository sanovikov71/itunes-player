package com.gmail.sanovikov71.intechtask.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.gmail.sanovikov71.intechtask.R;
import com.gmail.sanovikov71.intechtask.network.Song;

public class PlayerActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    public static final String EXTRA_SONG = "EXTRA_SONG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        PlayerFragment fragment = (PlayerFragment)
                getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        MusicHelper musicHelper = new MusicHelper();
        Song song = getIntent().getParcelableExtra(EXTRA_SONG);
        if (song != null) {
            musicHelper.prepareMediaPlayer(song.getPreviewUrl(), fragment);
        }

        if (fragment == null) {
            fragment = PlayerFragment.newInstance(song);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.player_container, fragment, FRAGMENT_TAG)
                    .commit();
            fragment.setMusicHelper(musicHelper);
        }
    }

}
