package com.example.mp5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MP5:Main";
    private RequestQueue requestQueue;
    Spinner dietSpinner;
    Spinner cuisineSpinner;
    Spinner intolerancesSpinner;
    Spinner typeSpinner;
    ProgressBar progressBar;
    private static Map<String, Integer> toShow;
    private static List<JsonObject> individualResults;
    private static Map<String, Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button processImage = findViewById(R.id.doneButton);
        processImage.setOnClickListener(v -> onClick());
        dietSpinner = findViewById(R.id.dietSpinner);
        cuisineSpinner = findViewById(R.id.cuisineSpinner);
        intolerancesSpinner = findViewById(R.id.intolerancesSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        progressBar = findViewById(R.id.progressBar);
        toShow = new LinkedHashMap<>();
        ids = new LinkedHashMap<>();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cuisine_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineSpinner.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.diet_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietSpinner.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.intolerances_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intolerancesSpinner.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }
    protected void onClick() {
        Log.d(TAG, "Done button clicked.");
        progressBar.setVisibility(View.VISIBLE);
        new Tasks.ProcessTextTask(MainActivity.this, requestQueue).execute();
    }
    public String getCuisine() {
        String cuisine = cuisineSpinner.getSelectedItem().toString();
        if (cuisine.equals("Any")) {
            return null;
        }
        return cuisine;
    }
    public String getDiet() {
        String diet = dietSpinner.getSelectedItem().toString();
        if (diet.equals("Any")) {
            return null;
        }
        return diet;
    }
    public String getExclude() {
        TextInputEditText exclude = findViewById(R.id.excludeIngredientsInput);
        Editable excludeText = exclude.getText();
        if (excludeText == null || excludeText.length() == 0) {
            return null;
        }
        return excludeText.toString();
    }
    public String getIntolerances() {
        String intolerances = intolerancesSpinner.getSelectedItem().toString();
        if (intolerances.equals("Any")) {
            return null;
        }
        return intolerances;
    }
    public String getNumber() throws IllegalArgumentException {
        EditText recipeNumber = findViewById(R.id.numberInput);
        String numberAsString = recipeNumber.getText().toString();
        if (numberAsString.length() == 0) {
            return null;
        }
        int number = Integer.parseInt(numberAsString);
        if (number <= 0 || number > 100) {
            throw new IllegalArgumentException();
        }
        return Integer.toString(number);
    }
    public String getType() {
        String type = typeSpinner.getSelectedItem().toString();
        if (type.equals("Any")) {
            return null;
        }
        return type;
    }
    public String getQuery() {
        TextInputEditText query = findViewById(R.id.recipeSearch);
        Editable queryText = query.getText();
        if (queryText == null || queryText.length() == 0) {
            return null;
        }
        return queryText.toString();
    }
    public static Map<String, Integer> getToShow() {
        return toShow;
    }
    public static List<JsonObject> getIndividualResults() {
        return individualResults;
    }
    public static Map<String, Integer> getIds() { return ids; }
    public void finishProcessing(final String json) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonArray jsonResults = jsonParser.parse(json).getAsJsonObject().getAsJsonArray("results");
            individualResults = new ArrayList<>();
            for (int i = 0; i < jsonResults.size(); i++) {
                individualResults.add(jsonResults.get(i).getAsJsonObject());
            }
            for (int i = 0; i < individualResults.size(); i++) {
                toShow.put(individualResults.get(i).get("title").getAsString(), individualResults.get(i).get("readyInMinutes").getAsInt());
            }
            for (int i = 0; i < individualResults.size(); i++) {
                ids.put(individualResults.get(i).get("title").getAsString(), individualResults.get(i).get("id").getAsInt());
            }
            if (toShow.size() == 0) {
                throw new Exception();
            }
            Log.d(TAG, "Successful call to API.");
            Log.d(TAG, "toShow.size() == " + toShow.size());
            Intent intent = new Intent(this, ScrollingActivity.class);
            startActivity(intent);
        } catch(Exception e) {
            Log.e(TAG, "Json did not include recipes or inputs were invalid.");
            DialogFragment dialog = new AlertDialogFragment();
            dialog.show(getSupportFragmentManager(), "nullReturn");
        } finally {
            runOnUiThread(() -> progressBar.setVisibility(View.GONE));
        }
    }
    public static class AlertDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Your search returned no results or contained invalid inputs. Please try again.");
            builder.setPositiveButton("OK", (dialog, which) -> {});
            return builder.create();
        }
    }
}
