package com.android.popularmoviesstagetwo.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Eric Emery on 5/23/2017.
 */

public class MovieSyncIntentService extends IntentService {

    public MovieSyncIntentService() {
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MovieSyncTask.syncMovies(this);
    }
}
