package com.example.android.popularmovies.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.events.MoviesResponseEvent;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.repositories.MovieRepository;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> mMovies;
    private LiveData<List<Movie>> mFavoriteMoviesLiveData;
    private MovieRepository mMovieRepository;
    private EventBus mBus;

    public MainViewModel(Context context) {
        mMovies = new MutableLiveData<>();
        mMovieRepository = MovieRepository.getInstance(context);
        mBus = EventBus.getDefault();
        mBus.register(this);
    }

    public MutableLiveData<List<Movie>> getMovies() {
        if (mMovies == null) {
            mMovies = new MutableLiveData<>();
        }
        return mMovies;
    }

    public void fetchMovies(String sortType, int pageNumber) {
        mMovieRepository.getMovies(sortType, pageNumber);
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        if(mFavoriteMoviesLiveData == null) {
            mFavoriteMoviesLiveData = mMovieRepository.getFavoriteMovies();
        }
        return mFavoriteMoviesLiveData;
    }

    @Subscribe
    public void onEvent(MoviesResponseEvent event) {
        List<Movie> newList = new ArrayList<>();

        if(mMovies.getValue() != null && !event.isNewData()){
            newList.addAll(mMovies.getValue());
        }

        newList.addAll(event.getData());

        mMovies.postValue(newList);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mBus.unregister(this);
    }
}
