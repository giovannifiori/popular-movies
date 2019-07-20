package com.example.android.popularmovies.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.events.MoviesResponseEvent;
import com.example.android.popularmovies.repositories.MovieRepository;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainViewModel extends ViewModel {

    private MutableLiveData<MoviesResponseEvent> mMoviesEventLiveData;
    private MovieRepository mMovieRepository;
    private EventBus mBus;

    public MainViewModel() {
        mMoviesEventLiveData = new MutableLiveData<>();
        mMovieRepository = MovieRepository.getInstance();
        mBus = EventBus.getDefault();
        mBus.register(this);
    }

    public MutableLiveData<MoviesResponseEvent> getMovies() {
        if (mMoviesEventLiveData == null) {
            mMoviesEventLiveData = new MutableLiveData<>();
        }
        return mMoviesEventLiveData;
    }

    public void fetchMovies(String sortType, int pageNumber) {
        mMovieRepository.getMovies(sortType, pageNumber);
    }

    @Subscribe
    public void onEvent(MoviesResponseEvent event) {
        mMoviesEventLiveData.postValue(event);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mBus.unregister(this);
    }
}
