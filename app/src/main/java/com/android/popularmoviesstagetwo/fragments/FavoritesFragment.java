package com.android.popularmoviesstagetwo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

public class FavoritesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        PosterAdapter.MovieAdapterOnClickHandler {

    private final String TAG = FavoritesFragment.class.getSimpleName();

    private final String FROM = "from";
    private final String FAVORITE = "favorite";

    private Context mContext;

    private TextView mNoFavoritesTextView;
    private PosterAdapter mPosterAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mProgressBar;

    public static final String[] MAIN_MOVIE_PROJECTION = {
            DatabaseContract.FavoriteEntry.COLUMN_MOVIE_ID,
            DatabaseContract.FavoriteEntry.COLUMN_MOVIE_TITLE,
            DatabaseContract.FavoriteEntry.COLUMN_MOVIE_POSTER
    };

    private static final int FAVORITE_LOADER_ID = 13;


    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View favoriteRootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        mContext = getContext();
        mNoFavoritesTextView = (TextView) favoriteRootView.findViewById(R.id.tv_no_favorites);
        mRecyclerView = (RecyclerView) favoriteRootView.findViewById(R.id.rv_favorite);
        mProgressBar = (ProgressBar) favoriteRootView.findViewById(R.id.pb_favorite);

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

        getLoaderManager().initLoader(FAVORITE_LOADER_ID, null, this);

        return favoriteRootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case FAVORITE_LOADER_ID:
                Uri favoriteQueryUri = DatabaseContract.FavoriteEntry.FAVORITE_CONTENT_URI;
                String favoriteSortOrder = DatabaseContract.FavoriteEntry._ID + " ASC";
                return new CursorLoader(mContext,
                        favoriteQueryUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        favoriteSortOrder);
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
            noFavoritesView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPosterAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int movieID) {
        Intent movieDetailIntent = new Intent(getActivity(), MovieDetailsActivity.class);
        Uri uriForMovieClicked = DatabaseContract.FavoriteEntry.buildMovieUriWithID(movieID);
        movieDetailIntent.setData(uriForMovieClicked);
        movieDetailIntent.putExtra(FROM, FAVORITE);
        Log.d(TAG, "URI:" + uriForMovieClicked);
        startActivity(movieDetailIntent);
    }

    private void showMovieDataView() {
        mNoFavoritesTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void noFavoritesView() {
        mNoFavoritesTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        mNoFavoritesTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }
}