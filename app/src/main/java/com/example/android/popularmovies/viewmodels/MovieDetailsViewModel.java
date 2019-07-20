package com.example.android.popularmovies.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.events.MovieReviewsEvent;
import com.example.android.popularmovies.events.MovieTrailersEvent;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.repositories.MovieRepository;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MovieDetailsViewModel extends ViewModel {

    private MutableLiveData<MovieTrailersEvent> mMovieTrailersLiveData;
    private MutableLiveData<MovieReviewsEvent> mMovieReviewsLiveData;
    private MovieRepository mMovieRepository;
    private EventBus mBus;

    public MovieDetailsViewModel(Context context) {
        mMovieTrailersLiveData = new MutableLiveData<>();
        mMovieReviewsLiveData = new MutableLiveData<>();
        mMovieRepository = MovieRepository.getInstance(context);
        mBus = EventBus.getDefault();
        mBus.register(this);
    }

    public MutableLiveData<MovieTrailersEvent> getMovieTrailers() {
        if (mMovieTrailersLiveData == null) {
            mMovieTrailersLiveData = new MutableLiveData<>();
        }
        return mMovieTrailersLiveData;
    }

    public MutableLiveData<MovieReviewsEvent> getMovieReviews() {
        if (mMovieReviewsLiveData == null) {
            mMovieReviewsLiveData = new MutableLiveData<>();
        }
        return mMovieReviewsLiveData;
    }

    public void fetchMovieTrailers(int movieId) {
        mMovieRepository.getMovieTrailers(movieId);
    }

    public void fetchMovieReviews(int movieId, int pageNumber) {
        mMovieRepository.getMovieReviews(movieId, pageNumber);
    }

    public void addToFavorites(Movie movie) {
        mMovieRepository.addToFavorites(movie);
    }

    public void removeFromFavorites(Movie movie) {
        mMovieRepository.removeFromFavorites(movie);
    }

    @Subscribe
    public void onEvent(MovieTrailersEvent event) {
        mMovieTrailersLiveData.postValue(event);
    }

    @Subscribe
    public void onEvent(MovieReviewsEvent event) {
        mMovieReviewsLiveData.postValue(event);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mBus.unregister(this);
    }
}
