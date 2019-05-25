package com.example.android.popularmovies.models;

import java.util.Date;

public class Movie {
    private int mId;
    private String mTitle;
    private String mPosterPath;
    private String mSynopsis;
    private Date mReleaseDate;
    private float mVoteAverage;
    private float mPopularity;

    Movie() {
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

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public float getVoteAverage() {
        return mVoteAverage;
    }

    public float getPopularity() {
        return mPopularity;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public void setSynopsis(String mSynopsis) {
        this.mSynopsis = mSynopsis;
    }

    public void setReleaseDate(Date mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public void setVoteAverage(float mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public void setPopularity(float mPopularity) {
        this.mPopularity = mPopularity;
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
