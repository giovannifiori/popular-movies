package com.example.android.popularmovies.services;

import com.example.android.popularmovies.services.responseModels.MovieAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMovieService {
    /**
     * Calls the API endpoint to list the top movies according to the sort type
     * @param sortType Sort type of movies list. Options are: "popular" or "top_rated"
     * @param apiKey TheMovieDB API key
     */
    @GET("/3/movie/{sortType}")
    Call<MovieAPIResponse> getMovies(
            @Path("sortType") String sortType,
            @Query("api_key") String apiKey,
            @Query("page") int pageNumber
    );
}
