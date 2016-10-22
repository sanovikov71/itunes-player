package com.gmail.sanovikov71.intechtask.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.gmail.sanovikov71.intechtask.R;

public class PlayerActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    public static final String EXTRA_SONG_ID = "EXTRA_SONG_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (fragment == null) {
            fragment = PlayerFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.player_container, fragment, FRAGMENT_TAG)
                    .commit();
        }
    }

}
