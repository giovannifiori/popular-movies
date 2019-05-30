package com.example.android.popularmovies.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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
    public static final String MOVIE_EXTRA = "com.example.android.popularmovies_movie";
    private static final String SORT_MOST_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private IMovieService mMovieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieService = ServiceUtils.getIMovieService();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRecyclerView = findViewById(R.id.rv_movies_list);
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        fetchMovies(SORT_MOST_POPULAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();

        if (selectedId == R.id.action_popularity) {
            fetchMovies(SORT_MOST_POPULAR);
        } else if (selectedId == R.id.action_top_rated) {
            fetchMovies(SORT_TOP_RATED);
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchMovies(@NonNull String sortType) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mMovieService.getMovies(sortType, BuildConfig.ApiKey).enqueue(new Callback<MovieAPIResponse>() {
            @Override
            public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mMoviesAdapter.setMoviesData(response.body().getMovies());
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.smoothScrollToPosition(0);
                } else {
                    Log.d(TAG, "onResponse: with error");
                }
            }

            @Override
            public void onFailure(Call<MovieAPIResponse> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onFailure: ERROR" + t);
            }
        });
    }

    @Override
    public void onClick(Movie selectedMovie) {
        Intent detailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        detailsIntent.putExtra(MOVIE_EXTRA, selectedMovie);
        startActivity(detailsIntent);
    }
}
