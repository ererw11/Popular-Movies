package com.android.popularmoviesstagetwo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.popularmoviesstagetwo.database.DatabaseContract.FavoriteEntry;
import com.android.popularmoviesstagetwo.database.DatabaseContract.HighRatedMovieEntry;
import com.android.popularmoviesstagetwo.database.DatabaseContract.MostPopMovieEntry;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies_and_favorites.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + MostPopMovieEntry.TABLE_NAME_MOST_POP + "(" +
                MostPopMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MostPopMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MostPopMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MostPopMovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MostPopMovieEntry.COLUMN_MOVIE_RATING + " DOUBLE NOT NULL, " +
                MostPopMovieEntry.COLUMN_MOVIE_RELEASE + " DATE NOT NULL, " +
                MostPopMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MostPopMovieEntry.COLUMN_MOVIE_BACKDROP + " TEXT NOT NULL)"
        );

        db.execSQL("CREATE TABLE " + HighRatedMovieEntry.TABLE_NAME_HIGH_RATED + "(" +
                HighRatedMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HighRatedMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                HighRatedMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                HighRatedMovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                HighRatedMovieEntry.COLUMN_MOVIE_RATING + " DOUBLE NOT NULL, " +
                HighRatedMovieEntry.COLUMN_MOVIE_RELEASE + " DATE NOT NULL, " +
                HighRatedMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                HighRatedMovieEntry.COLUMN_MOVIE_BACKDROP + " TEXT NOT NULL)"
        );

        db.execSQL("CREATE TABLE " + FavoriteEntry.TABLE_NAME_FAVORITE + "(" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_RATING + " DOUBLE NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_RELEASE + " DATE NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_BACKDROP + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_FAVORITE + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MostPopMovieEntry.TABLE_NAME_MOST_POP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HighRatedMovieEntry.TABLE_NAME_HIGH_RATED);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME_FAVORITE);
        onCreate(sqLiteDatabase);
    }
}
