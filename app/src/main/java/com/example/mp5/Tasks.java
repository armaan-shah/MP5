package com.example.mp5;

import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;

import java.lang.ref.WeakReference;

public class Tasks {

    private static final String TAG = "MP5:Tasks";
    static class ProcessImageTask extends AsyncTask<View, Void, Integer> {
        private static final String SUBSCRIPTION_KEY = BuildConfig.API_KEY;
        private static final String API_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
        private static final String FEATURES = "Results";
        private WeakReference<MainActivity> activityReference;
        private RequestQueue requestQueue;

        ProcessImageTask(final MainActivity context, final RequestQueue setRequestQueue) {
            activityReference = new WeakReference<>(context);
            requestQueue = setRequestQueue;
        }

        @Override
        protected void onPreExecute() {
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            ProgressBar progressBar = activity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        protected Integer doInBackground(View... currentView) {
            String request = Uri.parse(API_URL)
                    .buildUpon()
                    .appendQueryParameter("visualFeatures", FEATURES)
                    .build()
                    .toString();
            return 0;
        }
    }
}