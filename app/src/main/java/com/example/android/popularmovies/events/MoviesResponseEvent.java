package com.example.android.popularmovies.events;

import com.example.android.popularmovies.models.Movie;

import java.util.List;

public class MoviesResponseEvent {
    private List<Movie> mData;
    private boolean isNewData;
    private Throwable mError;

    public MoviesResponseEvent(List<Movie> data) {
        mData = data;
    }

    public MoviesResponseEvent(List<Movie> mData, boolean isNewData) {
        this.mData = mData;
        this.isNewData = isNewData;
    }

    public MoviesResponseEvent(Throwable t) {
        mError = t;
    }

    public List<Movie> getData() {
        return mData;
    }

    public Throwable getError() {
        return mError;
    }

    public boolean isNewData() {
        return isNewData;
    }
}
