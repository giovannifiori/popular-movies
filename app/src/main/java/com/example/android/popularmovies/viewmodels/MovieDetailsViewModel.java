package com.example.android.popularmovies.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.events.MovieTrailersEvent;
import com.example.android.popularmovies.repositories.MovieRepository;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MovieDetailsViewModel extends ViewModel {

    private MutableLiveData<MovieTrailersEvent> mMovieTrailersLiveData;
    private MovieRepository mMovieRepository;
    private EventBus mBus;

    public MovieDetailsViewModel() {
        mMovieTrailersLiveData = new MutableLiveData<>();
        mMovieRepository = MovieRepository.getInstance();
        mBus = EventBus.getDefault();
        mBus.register(this);
    }

    public MutableLiveData<MovieTrailersEvent> getMovieTrailers() {
        if (mMovieTrailersLiveData == null) {
            mMovieTrailersLiveData = new MutableLiveData<>();
        }
        return mMovieTrailersLiveData;
    }

    public void fetchMovieTrailers(int movieId) {
        mMovieRepository.getMovieTrailers(movieId);
    }

    @Subscribe
    public void onEvent(MovieTrailersEvent event) {
        mMovieTrailersLiveData.postValue(event);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mBus.unregister(this);
    }
}
