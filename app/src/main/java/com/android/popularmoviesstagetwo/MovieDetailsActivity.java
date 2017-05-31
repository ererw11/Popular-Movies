package com.android.popularmoviesstagetwo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.popularmoviesstagetwo.database.DatabaseContract.FavoriteEntry;
import com.android.popularmoviesstagetwo.database.DatabaseContract.HighRatedMovieEntry;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.android.popularmoviesstagetwo.database.DatabaseContract.MostPopMovieEntry;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String FROM = "from";
    private final String MOST_POPULAR = "most_popular";
    private final String HIGHEST_RATED = "highest_rated";
    private final String FAVORITE = "favorite";

    public static final String[] MOST_POPULAR_PROJECTION = {
            MostPopMovieEntry._ID,
            MostPopMovieEntry.COLUMN_MOVIE_ID,
            MostPopMovieEntry.COLUMN_MOVIE_TITLE,
            MostPopMovieEntry.COLUMN_MOVIE_POSTER,
            MostPopMovieEntry.COLUMN_MOVIE_RATING,
            MostPopMovieEntry.COLUMN_MOVIE_RELEASE,
            MostPopMovieEntry.COLUMN_MOVIE_OVERVIEW,
            MostPopMovieEntry.COLUMN_MOVIE_BACKDROP
    };

    public static final String[] HIGH_RATED_PROJECTION = {
            HighRatedMovieEntry._ID,
            HighRatedMovieEntry.COLUMN_MOVIE_ID,
            HighRatedMovieEntry.COLUMN_MOVIE_TITLE,
            HighRatedMovieEntry.COLUMN_MOVIE_POSTER,
            HighRatedMovieEntry.COLUMN_MOVIE_RATING,
            HighRatedMovieEntry.COLUMN_MOVIE_RELEASE,
            HighRatedMovieEntry.COLUMN_MOVIE_OVERVIEW,
            HighRatedMovieEntry.COLUMN_MOVIE_BACKDROP
    };

    public static final String[] FAVORITE_PROJECTION = {
            FavoriteEntry._ID,
            FavoriteEntry.COLUMN_MOVIE_ID,
            FavoriteEntry.COLUMN_MOVIE_TITLE,
            FavoriteEntry.COLUMN_MOVIE_POSTER,
            FavoriteEntry.COLUMN_MOVIE_RATING,
            FavoriteEntry.COLUMN_MOVIE_RELEASE,
            FavoriteEntry.COLUMN_MOVIE_OVERVIEW,
            FavoriteEntry.COLUMN_MOVIE_BACKDROP,
            FavoriteEntry.COLUMN_MOVIE_FAVORITE
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_MOVIE_TITLE = 2;
    public static final int INDEX_MOVIE_POSTER = 3;
    public static final int INDEX_MOVIE_RATING = 4;
    public static final int INDEX_MOVIE_RELEASE = 5;
    public static final int INDEX_MOVIE_OVERVIEW = 6;
    public static final int INDEX_MOVIE_BACKDROP = 7;

    private static final int ID_DETAIL_LOADER = 111;

    private Uri mUri;

    private TextView movieDetailsTitle;
    private TextView movieDetailsRating;
    private TextView movieDetailDate;
    private TextView movieDetailOverview;
    private ImageView movieDetailsPoster;
    private ImageView movieDetailsBackdrop;
    private ImageView movieDetailsFavorite;

    private int mId;
    private int mMovieId;
    private String mMovieTitle;
    private String mMoviePoster;
    private String mMovieRating;
    private String mMovieReleaseDate;
    private String mMovieOverview;
    private String mMovieBackdrop;
    private String mUpdatedRating;

    private String mIsFavorite = "yes_favorite";

    final static String OLD_DATE_FORMAT = "yyyy-MM-dd";
    final static String NEW_DATE_FORMAT = "EEE MMM d, yyyy";

    final String BASE_URL_BACKDROP = "http://image.tmdb.org/t/p/w780/";
    final String BASE_URL_POSTER = "http://image.tmdb.org/t/p/w500/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieDetailsPoster = (ImageView) findViewById(R.id.iv_movie_poster_details);
        movieDetailsBackdrop = (ImageView) findViewById(R.id.iv_movie_backdrop);
        movieDetailsFavorite = (ImageView) findViewById(R.id.iv_movie_favorite);
        movieDetailsTitle = (TextView) findViewById(R.id.tv_movie_title_details);
        movieDetailsRating = (TextView) findViewById(R.id.tv_movie_rating);
        movieDetailDate = (TextView) findViewById(R.id.tv_movie_date);
        movieDetailOverview = (TextView) findViewById(R.id.tv_overview);

        mUri = getIntent().getData();

        movieDetailsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovieToFavorites(view);
            }
        });

        if (mUri == null)
            throw new NullPointerException("URI for MovieDetailsActivity cannot be null");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

    }

    public void addMovieToFavorites(View view) {
        //TODO delete method needs to be fixed
        if (getIntent().getStringExtra(FROM).equals (FAVORITE)) {
            ContentResolver contentResolver = getContentResolver();
            contentResolver.delete(FavoriteEntry.FAVORITE_CONTENT_URI, null, null);
            Toast.makeText(getBaseContext(), "Removed From Favorites", Toast.LENGTH_SHORT).show();
            finish();
        }
        ContentValues contentValues = new ContentValues();

        contentValues.put(FavoriteEntry.COLUMN_MOVIE_ID, mMovieId);
        contentValues.put(FavoriteEntry.COLUMN_MOVIE_TITLE, mMovieTitle);
        contentValues.put(FavoriteEntry.COLUMN_MOVIE_POSTER, mMoviePoster);
        contentValues.put(FavoriteEntry.COLUMN_MOVIE_RATING, mMovieRating);
        contentValues.put(FavoriteEntry.COLUMN_MOVIE_RELEASE, mMovieReleaseDate);
        contentValues.put(FavoriteEntry.COLUMN_MOVIE_OVERVIEW, mMovieOverview);
        contentValues.put(FavoriteEntry.COLUMN_MOVIE_BACKDROP, mMovieBackdrop);
        contentValues.put(FavoriteEntry.COLUMN_MOVIE_FAVORITE, mIsFavorite);

        Uri uri = getContentResolver().insert(FavoriteEntry.FAVORITE_CONTENT_URI, contentValues);

        if (uri != null) {
            Toast.makeText(getBaseContext(), mMovieTitle + " added to favorites", Toast.LENGTH_SHORT).show();
            }
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_DETAIL_LOADER:
                if (getIntent().getStringExtra(FROM).equals(MOST_POPULAR)) {
                    return new CursorLoader(this,
                            mUri,
                            MOST_POPULAR_PROJECTION,
                            null,
                            null,
                            null);
                } else if (getIntent().getStringExtra(FROM).equals(HIGHEST_RATED)) {
                    return new CursorLoader(this,
                            mUri,
                            HIGH_RATED_PROJECTION,
                            null,
                            null,
                            null);
                } else if (getIntent().getStringExtra(FROM).equals (FAVORITE)) {
                    return new CursorLoader(this,
                            mUri,
                            FAVORITE_PROJECTION,
                            null,
                            null,
                            null);
                }
            default:
                throw new RuntimeException("Loader not implemented" + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }
        if (!cursorHasValidData) {
            return;
        }

        int id = data.getInt(INDEX_ID);
        int movieID = data.getInt(INDEX_MOVIE_ID);
        String movieTitle = data.getString(INDEX_MOVIE_TITLE);
        String moviePoster = data.getString(INDEX_MOVIE_POSTER);
        String movieRating = data.getString(INDEX_MOVIE_RATING);
        String movieRelease = data.getString(INDEX_MOVIE_RELEASE);
        String movieOverview = data.getString(INDEX_MOVIE_OVERVIEW);
        String movieBackdrop = data.getString(INDEX_MOVIE_BACKDROP);

        mId = id;
        mMovieId = movieID;
        mMovieTitle = movieTitle;
        mMoviePoster = moviePoster;
        mMovieRating = movieRating;
        mMovieReleaseDate = movieRelease;
        mMovieOverview = movieOverview;
        mMovieBackdrop = movieBackdrop;

        movieDetailsTitle.setText(movieTitle);
        mUpdatedRating = movieRating + " / 10";
        movieDetailsRating.setText(mUpdatedRating);
        movieDetailDate.setText(convertDate(movieRelease));
        movieDetailOverview.setText(movieOverview);
        Picasso.with(getBaseContext())
                .load(BASE_URL_POSTER + moviePoster)
                .error(R.drawable.ic_error_black_48dp)
                .into(movieDetailsPoster);
        Picasso.with(getBaseContext())
                .load(BASE_URL_BACKDROP + movieBackdrop)
                .error(R.drawable.ic_error_black_48dp)
                .into(movieDetailsBackdrop);

        if (getIntent().getStringExtra(FROM).equals (FAVORITE)) {
            movieDetailsFavorite.setImageResource(R.drawable.ic_favorite_black_36dp);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private static String convertDate(String dateFromMovieObject) {
        SimpleDateFormat originalFormat = new SimpleDateFormat(OLD_DATE_FORMAT, Locale.US);
        SimpleDateFormat newFormat = new SimpleDateFormat(NEW_DATE_FORMAT);
        String dateString = "";
        try {
            dateString = newFormat.format(originalFormat.parse(dateFromMovieObject));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }


}
