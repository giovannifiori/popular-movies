package com.example.android.popularmovies.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.viewmodels.MovieDetailsViewModel;

public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Context mContext;

    public MovieDetailsViewModelFactory(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieDetailsViewModel(mContext);
    }
}
