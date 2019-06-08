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
import android.widget.TextView;
import android.widget.Toast;

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
    private String mLastSortedBy = null;
    private ProgressBar mLoadingIndicator;
    private ProgressBar mListLoadingIndicator;
    private TextView mErrorMessageTextView;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private IMovieService mMovieService;
    private boolean mIsLoadingMovies = false;
    private int mMoviesPageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieService = ServiceUtils.getIMovieService();

        initializeComponents();

        fetchMovies(SORT_MOST_POPULAR, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();

        switch (selectedId) {
            case R.id.action_refresh:
                fetchMovies(mLastSortedBy, false);
                break;
            case R.id.action_popularity:
                fetchMovies(SORT_MOST_POPULAR, false);
                break;
            case R.id.action_top_rated:
                fetchMovies(SORT_TOP_RATED, false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void showContent() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void fetchMovies(@NonNull String sortType, final boolean append) {
        mIsLoadingMovies = true;
        mLastSortedBy = sortType;
        if (!append) {
            mMoviesPageNumber = 0;
            mRecyclerView.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mErrorMessageTextView.setVisibility(View.INVISIBLE);
        }

        mMovieService.getMovies(sortType, BuildConfig.ApiKey, ++mMoviesPageNumber).enqueue(new Callback<MovieAPIResponse>() {
            @Override
            public void onResponse(Call<MovieAPIResponse> call, Response<MovieAPIResponse> response) {
                mIsLoadingMovies = false;
                mListLoadingIndicator.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response.body() != null) {
                    mMoviesAdapter.setMoviesData(response.body().getMovies(), append);
                    showContent();
                    if (!append) {
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                } else {
                    Log.d(TAG, "onResponse: with error");
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<MovieAPIResponse> call, Throwable t) {
                mIsLoadingMovies = false;
                mListLoadingIndicator.setVisibility(View.INVISIBLE);
                if (!append) {
                    showErrorMessage();
                } else {
                    Toast.makeText(MainActivity.this, "Error on fetching more movies.", Toast.LENGTH_SHORT).show();
                }
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

    private void initializeComponents() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mListLoadingIndicator = findViewById(R.id.pb_list_loading_indicator);
        mRecyclerView = findViewById(R.id.rv_movies_list);
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);
        mErrorMessageTextView = findViewById(R.id.tv_error_message);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mRecyclerView.canScrollVertically(1) && !mIsLoadingMovies) {
                    mListLoadingIndicator.setVisibility(View.VISIBLE);
                    fetchMovies(mLastSortedBy, true);
                }
            }
        });
    }
}
