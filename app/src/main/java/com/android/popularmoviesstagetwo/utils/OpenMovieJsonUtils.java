package com.android.popularmoviesstagetwo.utils;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.popularmoviesstagetwo.database.DatabaseContract.MostPopMovieEntry.*;

/**
 * Created by Eric Emery on 5/18/2017.
 */

public class OpenMovieJsonUtils {

    public static ContentValues[] getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String RESULTS = "results";
        final String TITLE = "title";
        final String POSTER_PATH = "poster_path";
        final String VOTE_AVERAGE = "vote_average";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String BACKDROP_PATH = "backdrop_path";
        final String MOVIE_ID = "id";


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = movieJson.getJSONArray(RESULTS);
        ContentValues[] movieContentValues = new ContentValues[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {

            String movieID;
            String movieTitle;
            String moviePoster;
            String movieVoteAverage;
            String movieOverview;
            String movieReleaseDate;
            String movieBackdrop;

            JSONObject currentMovie = moviesArray.getJSONObject(i);
            movieID = currentMovie.getString(MOVIE_ID);
            movieTitle = currentMovie.getString(TITLE);
            moviePoster = currentMovie.getString(POSTER_PATH);
            movieVoteAverage = currentMovie.getString(VOTE_AVERAGE);
            movieOverview = currentMovie.getString(OVERVIEW);
            movieReleaseDate = currentMovie.getString(RELEASE_DATE);
            movieBackdrop =  currentMovie.getString(BACKDROP_PATH);

            ContentValues movieValues = new ContentValues();
            movieValues.put(COLUMN_MOVIE_ID, movieID);
            movieValues.put(COLUMN_MOVIE_TITLE, movieTitle);
            movieValues.put(COLUMN_MOVIE_POSTER, moviePoster);
            movieValues.put(COLUMN_MOVIE_RATING, movieVoteAverage);
            movieValues.put(COLUMN_MOVIE_OVERVIEW, movieOverview);
            movieValues.put(COLUMN_MOVIE_RELEASE, movieReleaseDate);
            movieValues.put(COLUMN_MOVIE_BACKDROP, movieBackdrop);
            movieContentValues[i] = movieValues;

        }
        return movieContentValues;
    }
}
