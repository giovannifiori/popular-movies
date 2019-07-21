package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.MovieReview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewViewHolder> {
    private List<MovieReview> mReviews;

    public MovieReviewsAdapter(List<MovieReview> reviews) {
        mReviews = reviews;
    }

    public MovieReviewsAdapter() {
        mReviews = new ArrayList<>();
    }

    class MovieReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_author)
        TextView mAuthorTextView;

        @BindView(R.id.tv_content)
        TextView mContentTextView;

        MovieReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_review_list_item, parent, false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder holder, int position) {
        MovieReview review = mReviews.get(position);
        holder.mAuthorTextView.setText(review.getAuthor());
        holder.mContentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void setReviews(@NonNull List<MovieReview> reviews) {
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }
}
