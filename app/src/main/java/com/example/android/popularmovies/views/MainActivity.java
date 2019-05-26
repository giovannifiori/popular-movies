package com.example.android.popularmovies.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.services.IMovieService;
import com.example.android.popularmovies.services.ServiceUtils;
import com.example.android.popularmovies.services.responseModels.MovieAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.PosterClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private IMovieService mMovieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieService = ServiceUtils.getIMovieService();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);

        mRecyclerView = findViewById(R.id.rv_movies_list);
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        fetchMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchMovies() {
        mMovieService.getMovies("popular", BuildConfig.ApiKey).enqueue(new Callback<MovieAPIResponse>() {
            @Override
            public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mMoviesAdapter.setMoviesData(response.body().getMovies());
                } else {
                    Log.d(TAG, "onResponse: with error");
                }
            }

            @Override
            public void onFailure(Call<MovieAPIResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ERROR" + t);
            }
        });
    }

    @Override
    public void onClick(Movie selectedMovie) {
        Log.d(TAG, "onClick: " + selectedMovie);
    }
}
