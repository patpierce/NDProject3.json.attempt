package com.example.android.pjbakersbuzzin.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static String[][] getRecipeStringsFromJson(Context context, String jsonRecipeResponse)
            throws JSONException {

        // keynames used in json data
        final String BAKEAPI_COL_ID = "id";
        final String BAKEAPI_COL_NAME = "name";
        final String BAKEAPI_COL_INGREDIENTS = "ingredients";
        final String BAKEAPI_COL_STEPS = "steps";
        final String BAKEAPI_COL_SERVINGS = "servings";
        final String BAKEAPI_COL_IMAGE = "image";
        final String BAKEAPI_COL_MESSAGE_CODE = "cod";

        // webservice provided jsonRecipeResponse which is json array
        JSONArray recipeJsonArray = new JSONArray(jsonRecipeResponse);
        JSONObject recipeJson = new JSONObject();
        // put the array into the json object to test error code
        recipeJson.put("arrayName",recipeJsonArray);

        /* Is there an error? */
        if (recipeJson.has(BAKEAPI_COL_MESSAGE_CODE)) {
            int errorCode = recipeJson.getInt(BAKEAPI_COL_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        Log.d(TAG, "getRecipeStringsFromJson: ");
        /* Two dimensional String array to hold each recipe's parsed attributes */
        String parsedRecipeData[][];
        parsedRecipeData = new String[recipeJsonArray.length()][6];

        for (int i = 0; i < recipeJsonArray.length(); i++) {
            String recipeId;
            String name;
            String ingredients;
            String steps;
            String servings;
            String image;

            /* Get the JSON object representing the individual recipe */
            JSONObject recipeObject = recipeJsonArray.getJSONObject(i);

            recipeId = recipeObject.getString(BAKEAPI_COL_ID);
            name = recipeObject.getString(BAKEAPI_COL_NAME);
            ingredients = recipeObject.getString(BAKEAPI_COL_INGREDIENTS);
            steps = recipeObject.getString(BAKEAPI_COL_STEPS);
            servings = recipeObject.getString(BAKEAPI_COL_SERVINGS);
            image = recipeObject.getString(BAKEAPI_COL_IMAGE);

            parsedRecipeData[i][0] = recipeId;
            parsedRecipeData[i][1] = name;
            parsedRecipeData[i][2] = ingredients;
            parsedRecipeData[i][3] = steps;
            parsedRecipeData[i][4] = servings;
            parsedRecipeData[i][5] = image;
        }

        return parsedRecipeData;
    }

}
