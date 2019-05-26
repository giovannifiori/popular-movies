package com.example.android.popularmovies.models;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("id")
    private int mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("overview")
    private String mSynopsis;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("vote_average")
    private float mVoteAverage;

    @SerializedName("popularity")
    private float mPopularity;

    public Movie() {
    }

    public Movie(int mId, String mTitle, String mPosterPath, String mSynopsis, String mReleaseDate, float mVoteAverage, float mPopularity) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mPosterPath = mPosterPath;
        this.mSynopsis = mSynopsis;
        this.mReleaseDate = mReleaseDate;
        this.mVoteAverage = mVoteAverage;
        this.mPopularity = mPopularity;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public float getVoteAverage() {
        return mVoteAverage;
    }

    public float getPopularity() {
        return mPopularity;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mPosterPath='" + mPosterPath + '\'' +
                ", mSynopsis='" + mSynopsis + '\'' +
                ", mReleaseDate=" + mReleaseDate +
                ", mVoteAverage=" + mVoteAverage +
                ", mPopularity=" + mPopularity +
                '}';
    }
}
