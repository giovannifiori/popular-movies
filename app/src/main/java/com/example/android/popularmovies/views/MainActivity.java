package com.example.android.popularmovies.views;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.events.MoviesResponseEvent;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.viewmodels.MainViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.PosterClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String MOVIE_EXTRA = "com.example.android.popularmovies_movie";

    private @FetchMode
    String mLastFetchBy = NONE;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.pb_list_loading_indicator)
    ProgressBar mListLoadingIndicator;

    @BindView(R.id.tv_error_message)
    TextView mErrorMessageTextView;

    @BindView(R.id.rv_movies_list)
    RecyclerView mRecyclerView;

    private MoviesAdapter mMoviesAdapter;
    private boolean mIsLoadingMovies = false;
    private int mMoviesPageNumber = 1;

    private MainViewModel mViewModel;

    @StringDef({NONE, FETCH_MOST_POPULAR, FETCH_TOP_RATED, FETCH_FAVORITES})
    private @interface FetchMode {
    }

    private static final String NONE = "";
    private static final String FETCH_MOST_POPULAR = "popular";
    private static final String FETCH_TOP_RATED = "top_rated";
    private static final String FETCH_FAVORITES = "favorites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        setupViewModel();

        fetchMovies(FETCH_MOST_POPULAR);
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
                resetPageCount();
                fetchMovies(mLastFetchBy);
                return true;
            case R.id.action_popularity:
                resetPageCount();
                fetchMovies(FETCH_MOST_POPULAR);
                return true;
            case R.id.action_top_rated:
                resetPageCount();
                fetchMovies(FETCH_TOP_RATED);
                return true;
            case R.id.action_favorites:
                //TODO implement method to get favorite movies from local database
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isFirstPage() {
        return mMoviesPageNumber == 1;
    }

    private void resetPageCount() {
        mMoviesPageNumber = 1;
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

    private void fetchMovies(@FetchMode String fetchMode) {
        mIsLoadingMovies = true;
        mLastFetchBy = fetchMode;
        if (isFirstPage()) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mErrorMessageTextView.setVisibility(View.INVISIBLE);
        }

        mViewModel.fetchMovies(fetchMode, mMoviesPageNumber);
    }

    @Override
    public void onClick(Movie selectedMovie) {
        Intent detailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        detailsIntent.putExtra(MOVIE_EXTRA, selectedMovie);
        startActivity(detailsIntent);
    }

    private void initView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mRecyclerView.canScrollVertically(1) && !mIsLoadingMovies) {
                    mListLoadingIndicator.setVisibility(View.VISIBLE);
                    fetchMovies(mLastFetchBy);
                }
            }
        });
    }

    private void setupViewModel() {
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        }

        subscribeDataObservers();
    }

    private void subscribeDataObservers() {
        mViewModel.getMovies().observe(this, this::handleMoviesResponse);
    }

    private void handleMoviesResponse(MoviesResponseEvent event) {
        mIsLoadingMovies = false;
        mListLoadingIndicator.setVisibility(View.INVISIBLE);

        if (event.getError() != null) {
            if (isFirstPage())
                showErrorMessage();
            else
                Toast.makeText(MainActivity.this, "Error on fetching more movies.", Toast.LENGTH_LONG).show();
            return;
        }

        if (isFirstPage())
            mMoviesAdapter.setMovies(event.getData());
        else
            mMoviesAdapter.appendMovies(event.getData());
        showContent();
        if (isFirstPage()) {
            mRecyclerView.smoothScrollToPosition(0);
        }
        mMoviesPageNumber += 1;
    }
}
