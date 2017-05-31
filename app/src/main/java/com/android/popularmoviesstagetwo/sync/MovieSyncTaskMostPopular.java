package com.android.popularmoviesstagetwo.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.android.popularmoviesstagetwo.database.DatabaseContract;
import com.android.popularmoviesstagetwo.utils.NetworkUtils;
import com.android.popularmoviesstagetwo.utils.OpenMovieJsonUtils;

import java.net.URL;

public class MovieSyncTaskMostPopular {

    synchronized public static void syncMostPopMovies(Context context) {
        try {
            URL mostPopMovieRequestUrl = NetworkUtils.buildMostPopUrl();
            String jsonMostPopularResponse = NetworkUtils.getResponseFromHttpUrl(mostPopMovieRequestUrl);
            ContentValues[] movieValues = OpenMovieJsonUtils
                    .getSimpleMovieStringsFromJson(context, jsonMostPopularResponse);
            if (movieValues != null && movieValues.length != 0){
                ContentResolver movieContentResolver = context.getContentResolver();
                movieContentResolver.delete(
                        DatabaseContract.MostPopMovieEntry.MOST_POP_CONTENT_URI,
                        null,
                        null);

                movieContentResolver.bulkInsert(
                        DatabaseContract.MostPopMovieEntry.MOST_POP_CONTENT_URI,
                        movieValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
