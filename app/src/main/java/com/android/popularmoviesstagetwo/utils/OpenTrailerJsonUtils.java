package com.android.popularmoviesstagetwo.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class OpenTrailerJsonUtils {

    public static String[] getSimpleTrailerStringsFromJson(String trailerJsonString) throws JSONException {

        final String TRAILER_RESULTS = "results";

        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";

        JSONObject trailerJson = new JSONObject(trailerJsonString);

        String[] parsedTrailerData = null;

        JSONArray trailerArray = trailerJson.getJSONArray(TRAILER_RESULTS);

        parsedTrailerData = new String[trailerArray.length()];

        for (int i = 0; i < trailerArray.length(); i++) {
            String key;
            String name;

            JSONObject thisTrailer = trailerArray.getJSONObject(i);
            key = thisTrailer.getString(TRAILER_KEY);
            name = thisTrailer.getString(TRAILER_NAME);

            parsedTrailerData[i] = key + " : " + name;

        }
        return parsedTrailerData;
    }
}
