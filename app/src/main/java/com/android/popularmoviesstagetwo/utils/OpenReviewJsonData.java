package com.android.popularmoviesstagetwo.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class OpenReviewJsonData {

    public static String[] getSimpleReviewStringsFromJson(String reviewJsonString) throws JSONException {

        final String REVIEWS_RESULTS = "results";

        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";

        JSONObject reviewJson = new JSONObject(reviewJsonString);

        String[] parsedReviewData = null;

        JSONArray reviewArray = reviewJson.getJSONArray(REVIEWS_RESULTS);

        parsedReviewData = new String[reviewArray.length()];

        for (int i = 0; i < reviewArray.length(); i++) {
            String author;
            String text;

            JSONObject thisReview = reviewArray.getJSONObject(i);

            author = thisReview.getString(REVIEW_AUTHOR);
            text = thisReview.getString(REVIEW_CONTENT);

            parsedReviewData[i] = author + " : " + text;
        }
        return parsedReviewData;
    }
}
