package com.example.android.popularmovies.services.responseModels;

import com.example.android.popularmovies.models.MovieReview;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewsResponse {

    @SerializedName("results")
    private List<MovieReview> reviews;

    public MovieReviewsResponse() {
    }

    public List<MovieReview> getReviews() {
        return reviews;
    }
}
