package com.android.popularmoviesstagetwo.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.android.popularmoviesstagetwo.database.DatabaseContract;
import com.android.popularmoviesstagetwo.database.DatabaseContract.HighRatedMovieEntry;
import com.android.popularmoviesstagetwo.database.DatabaseContract.MostPopMovieEntry;
import com.android.popularmoviesstagetwo.utils.NetworkUtils;
import com.android.popularmoviesstagetwo.utils.OpenMovieJsonUtils;

import java.net.URL;

public class MovieSyncTask {

    synchronized public static void syncMovies(Context context) {
        try {
            URL mostPopMovieRequestUrl = NetworkUtils.buildMostPopUrl();
            String jsonMostPopularResponse = NetworkUtils.getResponseFromHttpUrl(mostPopMovieRequestUrl);
            ContentValues[] mostPopularValues = OpenMovieJsonUtils.getSimpleMovieStringsFromJson(context, jsonMostPopularResponse);
            if (mostPopularValues != null && mostPopularValues.length != 0) {
                ContentResolver mostPopContentResolver = context.getContentResolver();
                mostPopContentResolver.delete(
                        MostPopMovieEntry.MOST_POP_CONTENT_URI,
                        null,
                        null);

                mostPopContentResolver.bulkInsert(
                        MostPopMovieEntry.MOST_POP_CONTENT_URI,
                        mostPopularValues);
            }

            URL highRatedMovieRequestUrl = NetworkUtils.buildHighRatedURL();
            String jsonHighRatedResponse = NetworkUtils.getResponseFromHttpUrl(highRatedMovieRequestUrl);
            ContentValues[] highRatedValues = OpenMovieJsonUtils.getSimpleMovieStringsFromJson(context, jsonHighRatedResponse);
            if (highRatedValues !=null && highRatedValues.length != 0) {
                ContentResolver highRatedContentResolver = context.getContentResolver();
                highRatedContentResolver.delete(
                        HighRatedMovieEntry.HIGH_RATED_CONTENT_URI,
                        null,
                        null);

                highRatedContentResolver.bulkInsert(
                        HighRatedMovieEntry.HIGH_RATED_CONTENT_URI,
                        highRatedValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
