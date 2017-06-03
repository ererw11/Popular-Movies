package com.android.popularmoviesstagetwo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.android.popularmoviesstagetwo.MainPosterActivity;
import com.android.popularmoviesstagetwo.MovieDetailsActivity;
import com.android.popularmoviesstagetwo.adapters.PosterAdapter;
import com.android.popularmoviesstagetwo.R;
import com.android.popularmoviesstagetwo.database.DatabaseContract;

public class HighestRatedFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        PosterAdapter.MovieAdapterOnClickHandler {

    private final String TAG = HighestRatedFragment.class.getSimpleName();

    private final String FROM = "from";
    private final String HIGHEST_RATED = "highest_rated";

    private Context mContext;

    private TextView mErrorTextView;
    private PosterAdapter mPosterAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;


    private ProgressBar mProgressBar;

    public static final String[] MAIN_MOVIE_PROJECTION = {
            DatabaseContract.HighRatedMovieEntry.COLUMN_MOVIE_ID,
            DatabaseContract.HighRatedMovieEntry.COLUMN_MOVIE_TITLE,
            DatabaseContract.HighRatedMovieEntry.COLUMN_MOVIE_POSTER
    };

    private static final int HIGH_RATED_LOADER_ID = 12;

    public static HighestRatedFragment newInstance() {
        HighestRatedFragment fragment = new HighestRatedFragment();
        return fragment;
    }

    public HighestRatedFragment() {
        // Required empty public constructor
    }

    //
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View highRatedRootView = inflater.inflate(R.layout.fragment_highest_rated, container, false);

        mContext = getContext();
        mErrorTextView = (TextView) highRatedRootView.findViewById(R.id.tv_error_message_highest_rated);
        mRecyclerView = (RecyclerView) highRatedRootView.findViewById(R.id.rv_highest_rated);
        mProgressBar = (ProgressBar) highRatedRootView.findViewById(R.id.pb_highest_rated);

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

        getLoaderManager().initLoader(HIGH_RATED_LOADER_ID, null, this);

        return highRatedRootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case HIGH_RATED_LOADER_ID:
                Uri movieQueryUri = DatabaseContract.HighRatedMovieEntry.HIGH_RATED_CONTENT_URI;
                String movieSortOrder = DatabaseContract.HighRatedMovieEntry._ID + " ASC";
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
        Uri uriForMovieClicked = DatabaseContract.HighRatedMovieEntry.buildMovieUriWithID(movieId);
        movieDetailIntent.setData(uriForMovieClicked);
        movieDetailIntent.putExtra(FROM, HIGHEST_RATED);
        Log.d(TAG, "URI: " + uriForMovieClicked);
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
