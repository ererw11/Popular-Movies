package com.android.popularmoviesstagetwo.database;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseContract {
    public static final String CONTENT_AUTHORITY = "com.android.popularmoviesstagetwo";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOST_POP = "most_pop";
    public static final String PATH_HIGH_RATED = "highest_rated";
    public static final String PATH_FAVORITE = "favorite";

    public static final class MostPopMovieEntry implements BaseColumns {

        public static final Uri MOST_POP_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOST_POP).build();

        public static final String TABLE_NAME_MOST_POP = "most_pop";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE = "release_date";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_BACKDROP = "backdrop";

        public static Uri buildMovieUriWithID(int movieId) {

            return MOST_POP_CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();
        }
    }

    public static final class HighRatedMovieEntry implements BaseColumns {

        public static final Uri HIGH_RATED_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HIGH_RATED).build();

        public static final String TABLE_NAME_HIGH_RATED = "highest_rated";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE = "release_date";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_BACKDROP = "backdrop";

        public static Uri buildMovieUriWithID(int movieId) {
            return HIGH_RATED_CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();
        }
    }

    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri FAVORITE_CONTENT_URI =
                DatabaseContract.BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String TABLE_NAME_FAVORITE = "favorite";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE = "release_date";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_BACKDROP = "backdrop";
        public static final String COLUMN_MOVIE_FAVORITE = "favorite";

        public static Uri buildMovieUriWithID(int movieId) {
            return FAVORITE_CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();

        }
    }
}
