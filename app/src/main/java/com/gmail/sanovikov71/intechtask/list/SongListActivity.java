package com.gmail.sanovikov71.intechtask.list;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.gmail.sanovikov71.intechtask.R;
import com.gmail.sanovikov71.intechtask.network.DataSource;
import com.gmail.sanovikov71.intechtask.network.MusicService;
import com.gmail.sanovikov71.intechtask.network.SearchResult;
import com.gmail.sanovikov71.intechtask.network.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongListActivity extends AppCompatActivity {

    private static final int MIN_TERM_LENGTH = 5;
    private static final String SEARCH_QUERY = "SEARCH_QUERY";

    private SongListAdapter mAdapter;
    private MusicService mMusicService;
    private String mSearchQuery;
    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        mMusicService = new DataSource().getService();

        final RecyclerView cardList = (RecyclerView) findViewById(R.id.song_list);
        final LinearLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new GridLayoutManager(this, 3);
        }
        cardList.setLayoutManager(layoutManager);
        mAdapter = new SongListAdapter(this);
        cardList.setAdapter(mAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY, mSearchQuery);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSearchQuery = savedInstanceState.getString(SEARCH_QUERY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addSearch(menu);
        addLookSwitcher();
        return true;
    }

    private void addSearch(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        final MenuItem search = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(search);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mSearchQuery = query;
                if (query.length() >= MIN_TERM_LENGTH) {
                    fetchSongList(query);
                }
                return false;
            }
        });
        mSearchView.setQuery(mSearchQuery, true);
    }

    private void addLookSwitcher() {
    }

    private void fetchSongList(String term) {
        mMusicService.searchMusic(term).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                final List<Song> searchResult = response.body().getResults();
                System.out.println("Novikov success");
                System.out.println("Novikov searchResult.size() : " + searchResult.size());
                mAdapter.updateDataset(searchResult);
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                System.out.println("Novikov failure");
            }
        });
    }

}
