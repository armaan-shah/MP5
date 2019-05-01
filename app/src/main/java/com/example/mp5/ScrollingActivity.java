package com.example.mp5;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.mp5.MainActivity.getIds;
import static com.example.mp5.MainActivity.getIndividualResults;
import static com.example.mp5.MainActivity.getToShow;

public class ScrollingActivity extends AppCompatActivity {

    static List<JsonObject> individualResults;
    static Map<String, Integer> toShow;
    static Map<String, Integer> ids;
    public static final String TAG = "MP5: Scrolling";
    RequestQueue requestQueue;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setContentView(R.layout.activity_scrolling);
        getFromMain();
        individualResults = new ArrayList<>();
        toShow = new LinkedHashMap<>();
        ids = new LinkedHashMap<>();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getFromMain() {
        individualResults = getIndividualResults();
        toShow = getToShow();
        ids = getIds();
        Log.e(TAG, "getFromMain done.");
        List<SpannableString> titles = new LinkedList<>();
        List<SpannableString> minutes = new LinkedList<>();
        toShow.forEach((k, v) -> titles.add(new SpannableString(k)));
        toShow.forEach((k, v) -> minutes.add(new SpannableString("Ready in " + v + " minutes.")));
        setDisplay(titles, minutes);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setDisplay(List<SpannableString> titles, List<SpannableString> minutes) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (int i = 0; i < titles.size(); i++) {
            titles.get(i).setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    new ScrollingTasks.ProcessTextTask(ScrollingActivity.this, requestQueue).execute();
                }
            }, 0, titles.get(i).length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.append(titles.get(i));
            builder.append('\n');
            builder.append(minutes.get(i));
            builder.append('\n');
            builder.append('\n');
        }
        TextView toDisplay = findViewById(R.id.display);
        toDisplay.setText(builder.toString());
        toDisplay.setElegantTextHeight(true);
    }
}
