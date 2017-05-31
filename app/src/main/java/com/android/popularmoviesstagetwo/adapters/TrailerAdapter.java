package com.android.popularmoviesstagetwo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.popularmoviesstagetwo.R;

/**
 * Created by Eric Emery on 5/23/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private String[] mTrailerData;

    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(String trailerData);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mTrailerPlayBtn;
        public final TextView mTrailerTitle;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerPlayBtn = (ImageView) view.findViewById(R.id.iv_trailer_play);
            mTrailerTitle = (TextView) view.findViewById(R.id.tv_trailer_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String trailerDataString = mTrailerData[adapterPosition];
            mClickHandler.onClick(trailerDataString);
        }
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        String trailerDataString = mTrailerData[position];
        // Separate the Title of this trailer from the data string
        String[] separateTitle = trailerDataString.split(":");
        String titleOfTrailer = separateTitle[1].trim();
        trailerAdapterViewHolder.mTrailerTitle.setText(titleOfTrailer);
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerData) return 0;
        return mTrailerData.length;
    }

    public void setTrailerData(String[] trailerDataString) {
        mTrailerData = trailerDataString;
        notifyDataSetChanged();
    }
}

