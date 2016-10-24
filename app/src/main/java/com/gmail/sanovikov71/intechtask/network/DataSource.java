package com.gmail.sanovikov71.intechtask.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataSource {

    private final MusicService mService;

    public DataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(MusicService.class);
    }

    public MusicService getService() {
        return mService;
    }

}
