package com.example.mp5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

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
    private Integer id;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        getFromMain();
        individualResults = new ArrayList<>();
        toShow = new LinkedHashMap<>();
        ids = new LinkedHashMap<>();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getFromMain() {
        individualResults = getIndividualResults();
        toShow = getToShow();
        ids = getIds();
        Log.d(TAG, "getFromMain done.");
        List<SpannableString> titles = new LinkedList<>();
        List<SpannableString> minutes = new LinkedList<>();
        toShow.forEach((k, v) -> titles.add(new SpannableString(k)));
        toShow.forEach((k, v) -> minutes.add(new SpannableString("Ready in " + v + " minutes.")));
        setDisplay(titles, minutes, ids);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setDisplay(List<SpannableString> titles, List<SpannableString> minutes, Map<String, Integer> ids) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        List<CustomClickableSpan> clickableSpans = new LinkedList<>();
        for (int i = 0; i < minutes.size(); i++) {
            clickableSpans.add(new CustomClickableSpan(titles.get(i)) {
                @Override
                public void onClick(@NonNull View widget) {
                    updateId(ids.get(getTitle()));
                    Log.d(TAG, getTitle());
                    new ScrollingTasks.ProcessTextTask(ScrollingActivity.this, requestQueue).execute();
                }
                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.BLUE);
                }
            });
            titles.get(i).setSpan(clickableSpans.get(i), 0, titles.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(titles.get(i));
            builder.append('\n');
            builder.append(minutes.get(i));
            builder.append('\n');
            builder.append('\n');

        }
        TextView toDisplay = findViewById(R.id.display);
        toDisplay.setText(builder);
        toDisplay.setElegantTextHeight(true);
        toDisplay.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void updateId(Integer setId) {
        id = setId;
    }
    public Integer getId() {
        Log.d(TAG, "getId");
        return this.id;
    }
    public void finishProcessing(final String json) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonPrimitive jsonResult;
            try {
                jsonResult = jsonParser.parse(json).getAsJsonObject().getAsJsonPrimitive("sourceUrl");
            } catch (Exception e) {
                jsonResult = jsonParser.parse(json).getAsJsonObject().getAsJsonPrimitive("spoonacularSourceUrl");
            }
            Intent URL = new Intent(android.content.Intent.ACTION_VIEW);
            URL.setData(Uri.parse(jsonResult.getAsString()));
            startActivity(URL);
        } catch (Exception e) {
            Log.e(TAG, "Invalid json.");
            DialogFragment dialog = new MainActivity.AlertDialogFragment();
            dialog.show(getSupportFragmentManager(), "jsonInvalid");
        }
    }
    public static class AlertDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Error. Please try another recipe.");
            builder.setPositiveButton("OK", (dialog, which) -> {});
            return builder.create();
        }
    }
}
