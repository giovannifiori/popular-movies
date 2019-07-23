package com.example.android.popularmovies.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskExecutor {

    private static final Object LOCK = new Object();
    private static DiskExecutor sInstance;
    private final Executor mDiskExecutor;

    private DiskExecutor(Executor diskExecutor) {
        mDiskExecutor = diskExecutor;
    }

    public static DiskExecutor getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new DiskExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskExecutor() {
        return mDiskExecutor;
    }

}
