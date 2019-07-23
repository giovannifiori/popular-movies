package com.example.android.popularmovies.views;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.android.popularmovies.viewmodels.factory.MainViewModelFactory;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.PosterClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LAST_FETCH_BUNDLE_KEY = "last-fetch-mode";
    private static final String LAST_PAGE_BUNDLE_KEY = "last-fetch-page";
    private static final String MOVIES_BUNDLE_KEY = "movies-bundle";
    private static final String LAYOUT_MANAGER_STATE_BUNDLE_KEY = "layout-manager-state";
    public static final String MOVIE_EXTRA = "com.example.android.popularmovies_movie";

    private @FetchMode
    String mLastFetchBy = FETCH_MOST_POPULAR;

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
    private boolean mIsActivityRecreated = false;
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LAST_FETCH_BUNDLE_KEY, mLastFetchBy);
        outState.putInt(LAST_PAGE_BUNDLE_KEY, mMoviesPageNumber);

        outState.remove(MOVIES_BUNDLE_KEY);
        Movie[] movieArr = new Movie[mMoviesAdapter.getItemCount()];
        outState.putParcelableArray(MOVIES_BUNDLE_KEY, mMoviesAdapter.getMovies().toArray(movieArr));

        if (mRecyclerView.getLayoutManager() != null) {
            outState.putParcelable(LAYOUT_MANAGER_STATE_BUNDLE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        setupViewModel();

        if (savedInstanceState != null) {
            mIsActivityRecreated = true;
            mLastFetchBy = savedInstanceState.getString(LAST_FETCH_BUNDLE_KEY, FETCH_MOST_POPULAR);
            mMoviesPageNumber = savedInstanceState.getInt(LAST_PAGE_BUNDLE_KEY, 1);

            Movie[] movieArr = (Movie[]) savedInstanceState.getParcelableArray(MOVIES_BUNDLE_KEY);
            mMoviesAdapter.setMovies(Arrays.asList(movieArr));

            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_BUNDLE_KEY);
            if (mRecyclerView.getLayoutManager() != null) {
                mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            }
        } else {
            mIsActivityRecreated = false;
            fetchMovies(mLastFetchBy);
        }
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
                mViewModel.getFavoriteMovies().removeObservers(this);
                resetPageCount();
                fetchMovies(FETCH_FAVORITES);
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

    private void showErrorMessage(@StringRes int errorString) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setText(errorString);
    }

    private void showLoadingIndicator() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
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
            showLoadingIndicator();
        }

        if (FETCH_FAVORITES.equals(fetchMode)) {
            mViewModel.getFavoriteMovies().observe(this, this::handleFavoriteMoviesResponse);
        } else {
            mViewModel.fetchMovies(fetchMode, mMoviesPageNumber);
        }
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
                if (!mRecyclerView.canScrollVertically(1) && !mIsLoadingMovies && !FETCH_FAVORITES.equals(mLastFetchBy)) {
                    mListLoadingIndicator.setVisibility(View.VISIBLE);
                    fetchMovies(mLastFetchBy);
                }
            }
        });
    }

    private void setupViewModel() {
        if (mViewModel == null) {
            MainViewModelFactory factory = new MainViewModelFactory(getApplicationContext());
            mViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
            subscribeDataObservers();
        }
    }

    private void subscribeDataObservers() {
        if (!mViewModel.getMovies().hasObservers()) {
            mViewModel.getMovies().observe(this, this::handleMoviesResponse);
        }
    }

    private void handleMoviesResponse(MoviesResponseEvent event) {
        mIsLoadingMovies = false;
        mListLoadingIndicator.setVisibility(View.INVISIBLE);

        if (mIsActivityRecreated) {
            mIsActivityRecreated = false;
            return;
        }

        if (event.getError() != null) {
            if (isFirstPage())
                showErrorMessage(R.string.error_message);
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

    private void handleFavoriteMoviesResponse(List<Movie> movies) {
        if (!FETCH_FAVORITES.equals(mLastFetchBy))
            return;

        mIsLoadingMovies = false;

        if (movies == null) {
            showErrorMessage(R.string.error_message);
            return;
        }

        if (movies.isEmpty()) {
            showErrorMessage(R.string.empty_favorites_list);
            return;
        }

        mMoviesAdapter.setMovies(movies);
        mRecyclerView.smoothScrollToPosition(0);
        showContent();
    }
}
