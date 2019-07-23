package com.example.android.popularmovies.models;

public class MovieTrailer {
    private final static String YOUTUBE_BASE_URL = "https://youtu.be/";

    private String name;
    private String source;
    private String type;
    private String size;

    public MovieTrailer() {
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getSourceFullUrl() {
        return YOUTUBE_BASE_URL + source;
    }

    public String getSourceToApp() {
        return "vnd.youtube:" + source;
    }

    @Override
    public String toString() {
        return "MovieTrailer{" +
                "name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
