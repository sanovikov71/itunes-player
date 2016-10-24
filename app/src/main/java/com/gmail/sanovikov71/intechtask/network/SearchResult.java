package com.gmail.sanovikov71.intechtask.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

    @SerializedName("resultCount")
    @Expose
    private Integer resultCount;
    @SerializedName("results")
    @Expose
    private List<Song> results = new ArrayList<>();

    /**
     * @return The resultCount
     */
    public Integer getResultCount() {
        return resultCount;
    }

    /**
     * @param resultCount The resultCount
     */
    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    /**
     * @return The results
     */
    public List<Song> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Song> results) {
        this.results = results;
    }

}