package com.example.android.popularmovies.events;

import com.example.android.popularmovies.models.MovieReview;

import java.util.List;

public class MovieReviewsEvent {
    private List<MovieReview> mData;
    private Throwable mError;

    public MovieReviewsEvent(List<MovieReview> data) {
        mData = data;
    }

    public MovieReviewsEvent(Throwable t) {
        mError = t;
    }

    public List<MovieReview> getData() {
        return mData;
    }

    public Throwable getError() {
        return mError;
    }
}
