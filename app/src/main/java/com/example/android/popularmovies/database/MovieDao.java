package com.example.android.popularmovies.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.android.popularmovies.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM favorite_movie")
    List<Movie> getFavoriteMovies();

    @Insert
    void addNewFavorite(Movie movie);

    @Delete
    void removeFromFavorites(Movie movie);
}
