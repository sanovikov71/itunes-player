package com.gmail.sanovikov71.intechtask.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicService {

    @GET("/search")
    Call<SearchResult> searchMusic(@Query("term") String searchKeyword);

}
