package com.example.android.popularmovies.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.services.ServiceUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mSynopsisTextView;
    private ImageView mPosterImageView;
    private Movie mMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MainActivity.MOVIE_EXTRA)) {
            mMovie = intent.getParcelableExtra(MainActivity.MOVIE_EXTRA);
        }

        initializeComponents();
        bindDataToComponents();
    }

    private void initializeComponents() {
        mPosterImageView = findViewById(R.id.iv_poster);
        mTitleTextView = findViewById(R.id.tv_title);
        mReleaseDateTextView = findViewById(R.id.tv_release_date);
        mVoteAverageTextView = findViewById(R.id.tv_vote_average);
        mSynopsisTextView = findViewById(R.id.tv_synopsis);
    }

    private void bindDataToComponents() {
        if (mMovie == null) return;
        mTitleTextView.setText(mMovie.getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        Date releaseDate = null;
        try {
            releaseDate = formatter.parse(mMovie.getReleaseDate());
        } catch (ParseException e) {
            Log.d(TAG, "bindDataToComponents: error parsing");
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
    }
}
