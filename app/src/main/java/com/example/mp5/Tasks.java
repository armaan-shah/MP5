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
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


class Tasks {

    private static final String TAG = "MP5:Tasks";
    static class ProcessTextTask extends AsyncTask<View, Void, Integer> {
        private static final String SUBSCRIPTION_KEY = BuildConfig.API_KEY;
        private static final String API_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/search?";
        private WeakReference<MainActivity> activityReference;
        private RequestQueue requestQueue;

        ProcessTextTask(final MainActivity context, final RequestQueue setRequestQueue) {
            activityReference = new WeakReference<>(context);
            requestQueue = setRequestQueue;
        }

        protected Integer doInBackground(View... currentView) {
            MainActivity activity = activityReference.get();
            Uri.Builder toRequestURL = Uri.parse(API_URL).buildUpon();
            String cuisine = activity.getCuisine();
            if (cuisine != null) {
                toRequestURL.appendQueryParameter("cuisine", cuisine);
            }
            String diet = activity.getDiet();
            if (diet != null) {
                toRequestURL.appendQueryParameter("diet", diet);
            }
            String exclude = activity.getExclude();
            if (exclude != null) {
                toRequestURL.appendQueryParameter("excludeIngredients", exclude);
            }
            String intolerances = activity.getIntolerances();
            if (intolerances != null) {
                toRequestURL.appendQueryParameter("intolerances", intolerances);
            }
            try {
                String number = activity.getNumber();
                if (number != null) {
                    toRequestURL.appendQueryParameter("number", number);
                }
            } catch (IllegalArgumentException e) {
                activity.finishProcessing(null);
            }
            String type = activity.getType();
            if (type != null) {
                toRequestURL.appendQueryParameter("type", type);
            }
            String query = activity.getQuery();
            if (query != null) {
                toRequestURL.appendQueryParameter("query", query);
            } else {
                Log.e(TAG, "Query is required but is null.");
                DialogFragment dialog = new AlertDialogFragment();
                dialog.show(activity.getSupportFragmentManager(), "blankQuery");
                activity.finishProcessing(null);
                return 0;
            }
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
            requestQueue.add(toRequest);
            return 0;
        }

        void handleApiResponse(final String response) {
            Log.d(TAG, "Response:" + response);
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.finishProcessing(response);
        }
        void handleApiError(final VolleyError error) {
            Log.w(TAG, "Error: " + error.toString());
            activityReference.get().finishProcessing(null);
        }
    }
    public static class AlertDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Food search cannot be blank.");
            builder.setPositiveButton("OK", (dialog, which) -> {});
            return builder.create();
        }
    }
}