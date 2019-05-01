package com.example.mp5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


class ScrollingTasks {

    private static final String TAG = "MP5:Tasks";
    static class ProcessTextTask extends AsyncTask<View, Void, Integer> {
        private static final String SUBSCRIPTION_KEY = BuildConfig.API_KEY;
        private static final String API_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/search?";
        private WeakReference<ScrollingActivity> activityReference;
        private RequestQueue requestQueue;

        ProcessTextTask(final ScrollingActivity context, final RequestQueue setRequestQueue) {
            activityReference = new WeakReference<>(context);
            requestQueue = setRequestQueue;
        }

        protected Integer doInBackground(View... currentView) {
            ScrollingActivity activity = activityReference.get();
            Uri.Builder toRequestURL = Uri.parse(API_URL).buildUpon();
            StringRequest toRequest = new StringRequest(
                    Request.Method.GET, toRequestURL.toString(),
                    this::handleApiResponse, this::handleApiError) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");
                    headers.put("X-RapidAPI-Key", SUBSCRIPTION_KEY);
                    return headers;
                }
            };
            Log.e(TAG, toRequest.toString());
            requestQueue.add(toRequest);
            return 0;
        }
        void handleApiResponse(final String response) {
            Log.d(TAG, "Response:" + response);
            ScrollingActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            //activity.finishProcessing(response);
        }
        void handleApiError(final VolleyError error) {
            Log.w(TAG, "Error: " + error.toString());
            //activityReference.get().finishProcessing(null);
        }
    }
}