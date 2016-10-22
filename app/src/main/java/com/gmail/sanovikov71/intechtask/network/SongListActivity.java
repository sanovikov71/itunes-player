package com.gmail.sanovikov71.intechtask.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataSource dataSource = new DataSource();
        final MusicService service = dataSource.getService();
        service.searchMusic("rihanna").enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                final List<Song> searchResult = response.body().getResults();
                System.out.println("Novikov success");
                System.out.println("Novikov searchResult.size() : " + searchResult.size());
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                System.out.println("Novikov failure");
            }
        });

    }

}
