package com.example.android.popularmovies.services.responseModels;

import com.example.android.popularmovies.models.MovieTrailer;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailersResponse {
    @SerializedName("youtube")
    private List<MovieTrailer> trailers;

    public MovieTrailersResponse() {
    }

    public List<MovieTrailer> getTrailers() {
        return trailers;
    }
}
