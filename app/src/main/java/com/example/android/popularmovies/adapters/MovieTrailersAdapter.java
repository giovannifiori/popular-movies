package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.MovieTrailer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MoviesAdapterViewHolder> {
    private List<MovieTrailer> mTrailers;
    private final TrailerOnClickListener mClickListener;

    public interface TrailerOnClickListener {
        /**
         * @param trailer Movie trailer that was clicked
         */
        void onClick(MovieTrailer trailer);
    }

    public MovieTrailersAdapter(List<MovieTrailer> trailers, TrailerOnClickListener listener) {
        mTrailers = trailers;
        mClickListener = listener;
        mTrailers = new ArrayList<>();
    }

    public MovieTrailersAdapter(TrailerOnClickListener listener) {
        mClickListener = listener;
        mTrailers = new ArrayList<>();
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_trailer_title)
        TextView mMovieTrailerTitle;

        MoviesAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MovieTrailer selectedTrailer = mTrailers.get(getAdapterPosition());
            mClickListener.onClick(selectedTrailer);
        }
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_trailer_list_item, parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        MovieTrailer trailer = mTrailers.get(position);
        String title = trailer.getType() + " - " + trailer.getName();
        holder.mMovieTrailerTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void setTrailers(@NonNull List<MovieTrailer> trailers) {
        mTrailers.clear();
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }
}
