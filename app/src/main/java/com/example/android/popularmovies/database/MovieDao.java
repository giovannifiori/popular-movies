package com.example.android.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.android.popularmovies.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM favorite_movie")
    LiveData<List<Movie>> getFavoriteMovies();

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    LiveData<Movie> getFavoriteMovie(int id);

    @Insert
    void addNewFavorite(Movie movie);

    @Delete
    void removeFromFavorites(Movie movie);
}
