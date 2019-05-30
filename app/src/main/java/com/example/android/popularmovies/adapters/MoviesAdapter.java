package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.services.ServiceUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private List<Movie> mMoviesData;
    private final PosterClickListener mClickListener;

    public interface PosterClickListener {
        /**
         *
         * @param selectedMovie Movie that was clicked
         */
        void onClick(Movie selectedMovie);
    }

    public MoviesAdapter(PosterClickListener listener) {
        mClickListener = listener;
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mMoviePosterImageView;

        MoviesAdapterViewHolder(View view) {
            super(view);
            mMoviePosterImageView = view.findViewById(R.id.iv_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Movie selectedMovie = mMoviesData.get(getAdapterPosition());
            mClickListener.onClick(selectedMovie);
        }
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movies_list_item, parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        Movie movie = mMoviesData.get(position);
        Picasso.get().setLoggingEnabled(true);
        if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
            Picasso.get()
                    .load(ServiceUtils.buildPosterUrl(movie.getPosterPath()).toString())
                    .placeholder(R.drawable.output)
                    .error(R.drawable.output)
                    .into(holder.mMoviePosterImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mMoviesData == null) return 0;
        return mMoviesData.size();
    }

    public void setMoviesData(@NonNull List<Movie> moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}
