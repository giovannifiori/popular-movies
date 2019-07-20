package com.example.android.popularmovies.repositories;

import androidx.annotation.NonNull;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.events.MovieReviewsEvent;
import com.example.android.popularmovies.events.MovieTrailersEvent;
import com.example.android.popularmovies.events.MoviesResponseEvent;
import com.example.android.popularmovies.exceptions.WebException;
import com.example.android.popularmovies.services.IMovieService;
import com.example.android.popularmovies.services.ServiceUtils;
import com.example.android.popularmovies.services.responseModels.MovieAPIResponse;
import com.example.android.popularmovies.services.responseModels.MovieReviewsResponse;
import com.example.android.popularmovies.services.responseModels.MovieTrailersResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static final String TAG = MovieRepository.class.getSimpleName();
    private static IMovieService mMovieService;
    private static EventBus mBus;
    private static MovieRepository sInstance;

    public static MovieRepository getInstance() {
        if (sInstance == null) {
            sInstance = new MovieRepository();
        }
        return sInstance;
    }

    private MovieRepository() {
        if (mMovieService == null) {
            mMovieService = ServiceUtils.getIMovieService();
        }
        mBus = EventBus.getDefault();
    }

    public void getMovies(String sortType, int pageNumber) {
        mMovieService.getMovies(sortType, BuildConfig.ApiKey, pageNumber).enqueue(new Callback<MovieAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieAPIResponse> call, @NonNull Response<MovieAPIResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mBus.post(new MoviesResponseEvent(response.body().getMovies()));
                    }
                } else {
                    mBus.post(new MoviesResponseEvent(new WebException(response.code())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieAPIResponse> call, @NonNull Throwable t) {
                mBus.post(new MoviesResponseEvent(t));
                t.printStackTrace();
            }
        });
    }

    public void getMovieTrailers(int movieId) {
        mMovieService.getMovieTrailers(movieId, BuildConfig.ApiKey).enqueue(new Callback<MovieTrailersResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieTrailersResponse> call, @NonNull Response<MovieTrailersResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mBus.post(new MovieTrailersEvent(response.body().getTrailers()));
                    }
                } else {
                    mBus.post(new MovieTrailersEvent(new WebException(response.code())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieTrailersResponse> call, @NonNull Throwable t) {
                mBus.post(new MovieTrailersEvent(t));
                t.printStackTrace();
            }
        });
    }

    public void getMovieReviews(int movieId, int pageNumber) {
        mMovieService.getMovieReviews(movieId, BuildConfig.ApiKey, pageNumber).enqueue(new Callback<MovieReviewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieReviewsResponse> call, @NonNull Response<MovieReviewsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mBus.post(new MovieReviewsEvent(response.body().getReviews()));
                    }
                } else {
                    mBus.post(new MovieReviewsEvent(new WebException(response.code())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieReviewsResponse> call, @NonNull Throwable t) {
                mBus.post(new MovieReviewsEvent(t));
                t.printStackTrace();
            }
        });
    }
}
