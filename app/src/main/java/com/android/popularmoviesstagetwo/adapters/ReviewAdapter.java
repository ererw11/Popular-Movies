package com.android.popularmoviesstagetwo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.popularmoviesstagetwo.R;

/**
 * Created by Eric Emery on 5/23/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private String[] mReviewData;

    private String WROTE = " wrote:";

    public ReviewAdapter() {
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mReviewAuthor;
        public final TextView mReviewText;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewAuthor = (TextView) view.findViewById(R.id.tv_review_author);
            mReviewText = (TextView) view.findViewById(R.id.tv_review);
        }
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        String reviewDataString = mReviewData[position];
        // Separate the author and text
        String[] separateAuthorAndText = reviewDataString.split(":");
        reviewAdapterViewHolder.mReviewAuthor.setText(separateAuthorAndText[0].trim() + WROTE);
        reviewAdapterViewHolder.mReviewText.setText(separateAuthorAndText[1].trim());
    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.length;
    }

    public void setReviewData(String[] reviewDataString) {
        mReviewData = reviewDataString;
        notifyDataSetChanged();
    }
}
