package com.example.android.popularmovies.events;

import com.example.android.popularmovies.models.Movie;

import java.util.List;

public class MoviesResponseEvent {
    private List<Movie> mData;
    private Throwable mError;

    public MoviesResponseEvent(List<Movie> data) {
        mData = data;
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
}
