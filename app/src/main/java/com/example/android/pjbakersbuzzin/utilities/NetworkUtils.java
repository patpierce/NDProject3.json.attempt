package com.example.android.pjbakersbuzzin.utilities;

//import android.net.Uri;

import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkUtils {

    private static final String RECIPE_LIST_URL = "http://go.udacity.com/android-baking-app-json";

    private NetworkUtils() { }

//    public static URL buildMainUrl(String sortOrder, String apiKey) {
//    public static URL buildMainUrl() {
//        Uri builtUri = Uri.parse(RECIPE_LIST_URL).buildUpon()
////                .appendPath(sortOrder)
////                .appendQueryParameter(API_KEY_PARAM, apiKey)
//                .build();
//
//        URL url = null;
//        try {
//            url = new URL(builtUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        return url;
//    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(RECIPE_LIST_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
