package com.example.android.popularmovies.exceptions;

public class WebException extends Exception {

    private int mStatusCode;

    public WebException(Throwable cause, int statusCode) {
        super(cause);
        mStatusCode = statusCode;
    }

    public WebException(Throwable cause) {
        super(cause);
    }

    public WebException(int statusCode) {
        mStatusCode = statusCode;
    }

    public int getStatusCode() {
        return mStatusCode;
    }
}
