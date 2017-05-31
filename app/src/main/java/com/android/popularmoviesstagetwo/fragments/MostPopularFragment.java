package com.android.popularmoviesstagetwo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.popularmoviesstagetwo.MovieDetailsActivity;
import com.android.popularmoviesstagetwo.adapters.PosterAdapter;
import com.android.popularmoviesstagetwo.R;
import com.android.popularmoviesstagetwo.database.DatabaseContract;
import com.android.popularmoviesstagetwo.sync.MovieSyncUtils;

public class MostPopularFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        PosterAdapter.MovieAdapterOnClickHandler {

    private final String TAG = MostPopularFragment.class.getSimpleName();

    private final String FROM = "from";
    private final String MOST_POPULAR = "most_popular";

    private Context mContext;

    private TextView mErrorTextView;
    private PosterAdapter mPosterAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mProgressBar;

    public static final String[] MAIN_MOVIE_PROJECTION = {
            DatabaseContract.MostPopMovieEntry.COLUMN_MOVIE_ID,
            DatabaseContract.MostPopMovieEntry.COLUMN_MOVIE_TITLE,
            DatabaseContract.MostPopMovieEntry.COLUMN_MOVIE_POSTER
    };

    private static final int MOVIE_LOADER_ID = 11;

    public static MostPopularFragment newInstance() {
        MostPopularFragment fragment = new MostPopularFragment();
        return fragment;
    }

   public MostPopularFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mostPopRootView = inflater.inflate(R.layout.fragment_most_popular, container, false);

        mContext = getContext();
        mErrorTextView = (TextView) mostPopRootView.findViewById(R.id.tv_error_message_most_popular);
        mRecyclerView = (RecyclerView) mostPopRootView.findViewById(R.id.rv_most_popular);
        mProgressBar = (ProgressBar) mostPopRootView.findViewById(R.id.pb_most_popular);

        LinearLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(mContext, 2);
        } else {
            layoutManager = new GridLayoutManager(mContext, 4);
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPosterAdapter = new PosterAdapter(mContext, this);
        mRecyclerView.setAdapter(mPosterAdapter);

        showLoading();

        getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);


        MovieSyncUtils.startMovieSync(mContext);

        return mostPopRootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case MOVIE_LOADER_ID:
                Uri movieQueryUri = DatabaseContract.MostPopMovieEntry.MOST_POP_CONTENT_URI;
                String movieSortOrder = DatabaseContract.MostPopMovieEntry._ID + " ASC";
                return new CursorLoader(getContext(),
                        movieQueryUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        movieSortOrder);
            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPosterAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) {
            showMovieDataView();
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPosterAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int movieId) {
        Intent movieDetailIntent = new Intent(getActivity(), MovieDetailsActivity.class);
        Uri uriForMovieClicked = DatabaseContract.MostPopMovieEntry.buildMovieUriWithID(movieId) ;
        movieDetailIntent.setData(uriForMovieClicked);
        movieDetailIntent.putExtra(FROM, MOST_POPULAR);
        Log.e(TAG, "URI: " + uriForMovieClicked);
        startActivity(movieDetailIntent);
    }

    private void showMovieDataView() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mErrorTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }
}
