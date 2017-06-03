package com.android.popularmoviesstagetwo.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.popularmoviesstagetwo.MainPosterActivity;
import com.android.popularmoviesstagetwo.MovieDetailsActivity;
import com.android.popularmoviesstagetwo.R;
import com.squareup.picasso.Picasso;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {

    private final Context mContext;

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(int movieId);
    }

    private Cursor mCursor;

    public PosterAdapter(@NonNull Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public PosterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.movie_list_item, parent, false);

        view.setFocusable(true);


        return new PosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        final String BASE_URL_POSTER = "http://image.tmdb.org/t/p/w500/";

        String movieTitle = mCursor.getString(MainPosterActivity.INDEX_MOVIE_TITLE);
        String moviePoster = mCursor.getString(MainPosterActivity.INDEX_MOVIE_POSTER);

        Picasso.with(mContext)
                .load(BASE_URL_POSTER + moviePoster)
                .error(R.drawable.ic_error_black_48dp)
                .into(holder.mPosterImageView);
        holder.mPosterTextView.setText(movieTitle);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mPosterImageView;
        public final TextView mPosterTextView;

        public PosterAdapterViewHolder(View view) {
            super(view);
            mPosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            mPosterTextView = (TextView) view.findViewById(R.id.tv_movie_title);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int movieID = mCursor.getInt(MainPosterActivity.INDEX_MOVIE_ID);
            Log.e("movieId", Integer.toString(movieID));
            mClickHandler.onClick(movieID);
        }
    }
}
