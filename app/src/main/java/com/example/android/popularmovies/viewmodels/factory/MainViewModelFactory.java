package com.example.android.popularmovies.viewmodels.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.viewmodels.MainViewModel;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Context mContext;

    public MainViewModelFactory(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(mContext);
    }
}
