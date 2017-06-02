package com.android.popularmoviesstagetwo.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private final static String SCHEME = "http";
    private final static String AUTHORITY = "api.themoviedb.org";
    private final static String APPEND_PATH_1 = "3";
    private final static String APPEND_PATH_2 = "movie";

    private final static String API_PARAM = "api_key";
    private final static String API_KEY = "";
    private final static String SORT_ORDER_POPULAR = "popular";
    private final static String SORT_ORDER_RATED = "top_rated";

    private final static String TRAILER_AND_REVIEW_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String TRAILER_URL_2 = "/videos?api_key=1e1522ebb1ae52a97fec91a11d731871&language=en-US";
    private final static String REVIEW_URL_2 =  "/reviews?api_key=1e1522ebb1ae52a97fec91a11d731871&language=en-US&page=1";


    public static URL buildMostPopUrl() {
        Uri.Builder builtUri = new Uri.Builder();
        // Create the URL for the Most Popular Movies
        builtUri.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(APPEND_PATH_1)
                .appendPath(APPEND_PATH_2)
                .appendPath(SORT_ORDER_POPULAR)
                .appendQueryParameter(API_PARAM, API_KEY)
                .toString();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Most Popular URI " + url);
        return url;
    }

    public static URL buildHighRatedURL() {
        Uri.Builder builtUri = new Uri.Builder();
        // Create the URL for the Top Rated Movies
        builtUri.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(APPEND_PATH_1)
                .appendPath(APPEND_PATH_2)
                .appendPath(SORT_ORDER_RATED)
                .appendQueryParameter(API_PARAM, API_KEY);

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Highest Rated URI " + url);
        return url;
    }

    public static URL buildTrailerUrl(String movieID) {

        String trailerUri = TRAILER_AND_REVIEW_BASE_URL + movieID + TRAILER_URL_2;

        URL url = null;

        try {
            url = new URL(trailerUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Trailer URL " + url);
        return url;
    }

    /* Build the URL for the Review JSON
   * */
    public static URL buildReviewUrl(String movieID) {

        String reviewUri = TRAILER_AND_REVIEW_BASE_URL + movieID + REVIEW_URL_2;

        URL url = null;

        try {
            url = new URL(reviewUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Review URL " + url);
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

