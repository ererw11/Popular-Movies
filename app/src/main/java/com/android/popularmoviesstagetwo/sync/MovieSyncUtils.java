package com.android.popularmoviesstagetwo.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class MovieSyncUtils {
    public static void startMovieSync(@NonNull final Context context) {
        Intent intent = new Intent(context, MovieSyncIntentService.class);
        context.startService(intent);
    }
}
