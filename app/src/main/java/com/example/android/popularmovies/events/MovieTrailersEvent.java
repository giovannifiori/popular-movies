package com.example.android.popularmovies.events;

import com.example.android.popularmovies.models.MovieTrailer;

import java.util.List;

public class MovieTrailersEvent {
    private List<MovieTrailer> mData;
    private Throwable mError;

    public MovieTrailersEvent(List<MovieTrailer> data) {
        mData = data;
    }

    public MovieTrailersEvent(Throwable t) {
        mError = t;
    }

    public List<MovieTrailer> getData() {
        return mData;
    }

    public Throwable getError() {
        return mError;
    }
}
