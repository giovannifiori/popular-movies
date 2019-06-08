package com.example.android.popularmovies.services;

import android.net.Uri;

import com.example.android.popularmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

public class ServiceUtils {
    public static final String API_BASE_URL = "https://api.themoviedb.org";
    public static final String API_POSTER_BASE_URL = "https://image.tmdb.org";
    private static final String API_KEY_PARAM = "api_key";

    /**
     * @return Movie API service to make HTTP requests
     */
    public static IMovieService getIMovieService() {
        return RetrofitClient.getClient(API_BASE_URL).create(IMovieService.class);
    }

    /**
     * Get the full URL of a poster
     *
     * @param posterPath Poster relative path
     * @return Poster's complete URL
     */
    public static URL buildPosterUrl(String posterPath) {
        if (posterPath == null) return null;
        Uri builtUri = Uri.parse(API_POSTER_BASE_URL).buildUpon()
                .path("/t/p/w342/" + posterPath)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.ApiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
