package com.example.android.popularmovies.services.responseModels;

import com.example.android.popularmovies.models.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieAPIResponse {
    @SerializedName("results")
    private List<Movie> movies;

    public MovieAPIResponse() {
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
