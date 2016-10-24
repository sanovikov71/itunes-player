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
    private static final String LAYOUT_LOOK = "LAYOUT_LOOK";

    private SongListAdapter mAdapter;
    private MusicService mMusicService;
    private String mSearchQuery;

    private RecyclerView mSongList;

    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mListLayoutManager;

    public static final int AS_GRID = 1;
    public static final int AS_LIST = 2;
    private int mLayoutLook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        if (savedInstanceState == null) {
            mLayoutLook = AS_GRID;
        }

        mMusicService = new DataSource().getService();

        mSongList = (RecyclerView) findViewById(R.id.song_list);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridLayoutManager = new GridLayoutManager(this, 2);
        } else {
            mGridLayoutManager = new GridLayoutManager(this, 3);
        }
        mListLayoutManager = new LinearLayoutManager(this);
        mSongList.setLayoutManager(mGridLayoutManager);
        mAdapter = new SongListAdapter(this);
        mSongList.setAdapter(mAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY, mSearchQuery);
        outState.putInt(LAYOUT_LOOK, mLayoutLook);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSearchQuery = savedInstanceState.getString(SEARCH_QUERY);
        mLayoutLook = savedInstanceState.getInt(LAYOUT_LOOK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addSearch(menu);
        addLookSwitcher(menu);
        return true;
    }

    private void addSearch(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        final MenuItem search = menu.findItem(R.id.action_search);
        final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(search);
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

    private void addLookSwitcher(Menu menu) {
        getMenuInflater().inflate(R.menu.change_list_look, menu);

        final MenuItem item = menu.findItem(R.id.change_list_look);
        if (mLayoutLook == AS_GRID) {
            useGridLayout(item);
        } else {
            useListLayout(item);
        }

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mLayoutLook == AS_GRID) {
                    useListLayout(item);
                } else {
                    useGridLayout(item);
                }
                return false;
            }
        });

    }

    private void useListLayout(MenuItem item) {
        mLayoutLook = AS_LIST;
        item.setTitle(getText(R.string.as_grid));
        mSongList.setLayoutManager(mListLayoutManager);
    }

    private void useGridLayout(MenuItem item) {
        mLayoutLook = AS_GRID;
        item.setTitle(getText(R.string.as_list));
        mSongList.setLayoutManager(mGridLayoutManager);
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
