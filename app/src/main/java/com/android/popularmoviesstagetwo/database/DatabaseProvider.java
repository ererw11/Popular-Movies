package com.android.popularmoviesstagetwo.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.popularmoviesstagetwo.database.DatabaseContract.FavoriteEntry;
import com.android.popularmoviesstagetwo.database.DatabaseContract.HighRatedMovieEntry;
import com.android.popularmoviesstagetwo.database.DatabaseContract.MostPopMovieEntry;

import static com.android.popularmoviesstagetwo.database.DatabaseContract.FavoriteEntry.TABLE_NAME_FAVORITE;

public class DatabaseProvider extends ContentProvider {

    public static final int CODE_MOST_POPULAR = 1100;
    public static final int CODE_MOST_POPULAR_WITH_ID = 1101;

    public static final int CODE_HIGH_RATED = 1200;
    public static final int CODE_HIGH_RATED_WITH_ID = 1201;

    public static final int CODE_FAVORITE = 1300;
    public static final int CODE_FAVORITES_WITH_ID = 1301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private DatabaseHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DatabaseContract.PATH_MOST_POP, CODE_MOST_POPULAR);
        matcher.addURI(authority, DatabaseContract.PATH_MOST_POP + "/#", CODE_MOST_POPULAR_WITH_ID);

        matcher.addURI(authority, DatabaseContract.PATH_HIGH_RATED, CODE_HIGH_RATED);
        matcher.addURI(authority, DatabaseContract.PATH_HIGH_RATED + "/#", CODE_HIGH_RATED_WITH_ID);

        matcher.addURI(authority, DatabaseContract.PATH_FAVORITE, CODE_FAVORITE);
        matcher.addURI(authority, DatabaseContract.PATH_FAVORITE + "/#", CODE_FAVORITES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_MOST_POPULAR:
                db.beginTransaction();
                int mostPopRowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MostPopMovieEntry.TABLE_NAME_MOST_POP, null, value);
                        if (_id != -1) {
                            mostPopRowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (mostPopRowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return mostPopRowsInserted;
            case CODE_HIGH_RATED:
                db.beginTransaction();
                int highRatedRowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(HighRatedMovieEntry.TABLE_NAME_HIGH_RATED, null, value);
                        if (_id != -1) {
                            highRatedRowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (highRatedRowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return highRatedRowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOST_POPULAR_WITH_ID: {
                String movieIDString = uri.getLastPathSegment();
                String [] selectionArguments = new String[]{movieIDString};
                cursor = mOpenHelper.getReadableDatabase().query(
                        MostPopMovieEntry.TABLE_NAME_MOST_POP,
                        projection,
                        MostPopMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_MOST_POPULAR: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        MostPopMovieEntry.TABLE_NAME_MOST_POP,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_HIGH_RATED_WITH_ID: {
                String movieIDString = uri.getLastPathSegment();
                String [] selectionArguments = new String[]{movieIDString};
                cursor = mOpenHelper.getReadableDatabase().query(
                        HighRatedMovieEntry.TABLE_NAME_HIGH_RATED,
                        projection,
                        HighRatedMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_HIGH_RATED: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        HighRatedMovieEntry.TABLE_NAME_HIGH_RATED,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_FAVORITES_WITH_ID: {
                String movieIDString = uri.getLastPathSegment();
                String [] selectionArguments = new String[]{movieIDString};
                cursor = mOpenHelper.getReadableDatabase().query(
                        TABLE_NAME_FAVORITE,
                        projection,
                        FavoriteEntry.COLUMN_MOVIE_ID + "= ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_FAVORITE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        TABLE_NAME_FAVORITE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE:
                long _id = db.insert(TABLE_NAME_FAVORITE, null, contentValues);
                if (_id != -1) {
                    returnUri = ContentUris.withAppendedId(FavoriteEntry.FAVORITE_CONTENT_URI, _id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int moviesDeleted;
        String id;

        switch (match) {
            case CODE_MOST_POPULAR:
                moviesDeleted = db.delete(
                        MostPopMovieEntry.TABLE_NAME_MOST_POP,
                        selection,
                        selectionArgs);
                break;
            case CODE_HIGH_RATED:
                moviesDeleted = db.delete(
                        HighRatedMovieEntry.TABLE_NAME_HIGH_RATED,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
