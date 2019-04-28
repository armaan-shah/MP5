package com.example.mp5;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MP5:Main";
    private RequestQueue requestQueue;
    Spinner dietSpinner;
    Spinner cuisineSpinner;
    Spinner intolerancesSpinner;
    Spinner typeSpinner;
    //ProgressBar progressBar;

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
        //progressBar.setVisibility(View.GONE);
    }
    protected void onClick() {
        Log.d(TAG, "Done button clicked.");
        //progressBar.setVisibility(View.VISIBLE);
        new Tasks.ProcessTextTask(MainActivity.this, requestQueue).execute();
    }
    public void sendMessage(View view) {
        Intent URL = new Intent(android.content.Intent.ACTION_VIEW);
        URL.setData(Uri.parse("http://www.google.com/"));
        Log.d(TAG, "Accessed Google.");
        startActivity(URL);
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
    public String getNumber() {
        EditText recipeNumber = findViewById(R.id.numberInput);
        String numberAsString = recipeNumber.getText().toString();
        if (numberAsString.length() == 0) {
            return null;
        }
        int number = Integer.parseInt(numberAsString);
        if (number < 0 || number > 100) {
            return null;
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
    public void finishProcessing(final String json) {
        try {
            TextView textView = findViewById(R.id.jsonResult);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(json);
            String prettyJsonString = gson.toJson(jsonElement);
            textView.setText(prettyJsonString);
            //progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
           // progressBar.setVisibility(View.GONE);
        }
    }
}
