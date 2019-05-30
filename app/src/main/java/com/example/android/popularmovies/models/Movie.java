package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
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

    public Movie(Parcel parcel) {
        mId = parcel.readInt();
        mTitle = parcel.readString();
        mPosterPath = parcel.readString();
        mSynopsis = parcel.readString();
        mReleaseDate = parcel.readString();
        mVoteAverage = parcel.readFloat();
        mPopularity = parcel.readFloat();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mSynopsis);
        dest.writeString(mReleaseDate);
        dest.writeFloat(mVoteAverage);
        dest.writeFloat(mPopularity);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
