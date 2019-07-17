package com.example.android.popularmovies.services;

import com.example.android.popularmovies.services.responseModels.MovieAPIResponse;
import com.example.android.popularmovies.services.responseModels.MovieReviewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMovieService {
    /**
     * Calls the API endpoint to list the top movies according to the sort type
     *
     * @param sortType Sort type of movies list. Options are: "popular" or "top_rated"
     * @param apiKey   TheMovieDB API key
     */
    @GET("/3/movie/{sortType}")
    Call<MovieAPIResponse> getMovies(
            @Path("sortType") String sortType,
            @Query("api_key") String apiKey,
            @Query("page") int pageNumber
    );

    /**
     * Calls the API endpoint to list the reviews of a movie
     *
     * @param movieId
     * @param apiKey     TheMovieDB API key
     * @param pageNumber
     */
    @GET("/3/movie/{id}/reviews")
    Call<MovieReviewsResponse> getMovieReviews(
            @Path("id") int movieId,
            @Query("api_key") String apiKey,
            @Query("page") int pageNumber
    );
}
