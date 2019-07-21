package com.example.android.popularmovies.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MovieTrailersAdapter;
import com.example.android.popularmovies.events.MovieReviewsEvent;
import com.example.android.popularmovies.events.MovieTrailersEvent;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.MovieTrailer;
import com.example.android.popularmovies.services.ServiceUtils;
import com.example.android.popularmovies.viewmodels.MovieDetailsViewModel;
import com.example.android.popularmovies.viewmodels.factory.MovieDetailsViewModelFactory;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailsActivity extends AppCompatActivity implements MovieTrailersAdapter.TrailerOnClickListener {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTextView;

    @BindView(R.id.tv_vote_average)
    TextView mVoteAverageTextView;

    @BindView(R.id.tv_synopsis)
    TextView mSynopsisTextView;

    @BindView(R.id.iv_poster)
    ImageView mPosterImageView;

    @BindView(R.id.button_add_to_favorites)
    Button mToggleFavoriteButton;

    @BindView(R.id.rv_trailers)
    RecyclerView mTrailersRecyclerView;

    @BindView(R.id.pb_trailers_loading_indicator)
    ProgressBar mTrailersLoadingIndicator;

    private MovieTrailersAdapter mTrailersAdapter;

    private int mReviewsPageNumber = 1;
    private boolean isFavorite;
    private Movie mMovie = null;
    private MovieDetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MainActivity.MOVIE_EXTRA)) {
            mMovie = intent.getParcelableExtra(MainActivity.MOVIE_EXTRA);
        }

        setupViewModel();
        initView();
        fetchMovieData();
    }

    private void setupViewModel() {
        if (mViewModel == null) {
            MovieDetailsViewModelFactory factory = new MovieDetailsViewModelFactory(getApplicationContext());
            mViewModel = ViewModelProviders.of(this, factory).get(MovieDetailsViewModel.class);
        }

        subscribeDataObservers();
    }

    private void subscribeDataObservers() {
        mViewModel.getMovieTrailers().observe(this, this::handleMovieTrailersEvent);
        mViewModel.getMovieReviews().observe(this, this::handleMovieReviewsEvent);
    }

    private void initView() {
        if (mMovie == null) return;

        mTitleTextView.setText(mMovie.getTitle());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        Date releaseDate = null;

        try {
            releaseDate = formatter.parse(mMovie.getReleaseDate());
        } catch (ParseException e) {
            Log.d(TAG, "initView: error parsing");
        }

        SimpleDateFormat newFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        mReleaseDateTextView.setText(
                String.format(Locale.getDefault(), getString(R.string.release_date),
                        releaseDate != null ? newFormatter.format(releaseDate) : mMovie.getReleaseDate()
                )
        );

        mVoteAverageTextView.setText(String.format(Locale.getDefault(), getString(R.string.vote_average), mMovie.getVoteAverage()));
        mSynopsisTextView.setText(mMovie.getSynopsis());

        Picasso.get()
                .load(ServiceUtils.buildPosterUrl(mMovie.getPosterPath()).toString())
                .into(mPosterImageView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mTrailersRecyclerView.setLayoutManager(layoutManager);

        mTrailersAdapter = new MovieTrailersAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        mTrailersRecyclerView.setNestedScrollingEnabled(false);
    }

    private void handleMovieTrailersEvent(MovieTrailersEvent event) {
        mTrailersLoadingIndicator.setVisibility(View.GONE);
        if (event.getError() != null) {
            Log.e(TAG, "handleMovieTrailersEvent: ", event.getError());
            return;
        }
        mTrailersAdapter.setTrailers(event.getData());
        mTrailersRecyclerView.setVisibility(View.VISIBLE);
    }

    private void handleMovieReviewsEvent(MovieReviewsEvent event) {
        if (event.getError() != null) {
            Log.e(TAG, "handleMovieReviewsEvent: ", event.getError());
            return;
        }
        mReviewsPageNumber += 1;
    }

    private void fetchMovieData() {
        mViewModel.fetchMovieTrailers(mMovie.getId());
        mViewModel.fetchMovieReviews(mMovie.getId(), mReviewsPageNumber);
        mViewModel.getMovieById(mMovie.getId()).observe(this, this::handleLocalMovieResponse);
    }

    private void handleLocalMovieResponse(Movie movie) {
        isFavorite = movie != null;
        if (isFavorite) {
            mToggleFavoriteButton.setText(R.string.remove_from_favorites);
        } else {
            mToggleFavoriteButton.setText(R.string.add_to_favorites);
        }
    }

    @OnClick(R.id.button_add_to_favorites)
    void onClick(View view) {
        if (isFavorite) {
            mViewModel.removeFromFavorites(mMovie);
        } else {
            mViewModel.addToFavorites(mMovie);
        }
    }

    @Override
    public void onClick(MovieTrailer trailer) {
        Intent watchTrailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getSourceToApp()));
        if (watchTrailerIntent.resolveActivity(getPackageManager()) == null) {
            watchTrailerIntent.setData(Uri.parse(trailer.getSourceFullUrl()));
        }
        startActivity(watchTrailerIntent);
    }
}
