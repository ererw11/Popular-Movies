package com.android.popularmoviesstagetwo.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.android.popularmoviesstagetwo.database.DatabaseContract;
import com.android.popularmoviesstagetwo.utils.NetworkUtils;
import com.android.popularmoviesstagetwo.utils.OpenMovieJsonUtils;

import java.net.URL;

public class MovieSyncTaskHighestRated {

    synchronized public static void syncHighRatedMovies(Context context) {
        try {
            URL highRatedMovieRequestUrl = NetworkUtils.buildHighRatedURL();
            String jsonHighRatedResponse = NetworkUtils.getResponseFromHttpUrl(highRatedMovieRequestUrl);
            ContentValues[] movieValues = OpenMovieJsonUtils
                    .getSimpleMovieStringsFromJson(context, jsonHighRatedResponse);
            if (movieValues != null && movieValues.length != 0){
                ContentResolver movieContentResolver = context.getContentResolver();
                movieContentResolver.delete(
                        DatabaseContract.HighRatedMovieEntry.HIGH_RATED_CONTENT_URI,
                        null,
                        null);

                movieContentResolver.bulkInsert(
                        DatabaseContract.HighRatedMovieEntry.HIGH_RATED_CONTENT_URI,
                        movieValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
