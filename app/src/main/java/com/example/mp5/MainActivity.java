package com.example.mp5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MP5:Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner dietSpinner = findViewById(R.id.dietSpinner);
        Spinner cuisineSpinner = findViewById(R.id.cuisineSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.diet_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietSpinner.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.cuisine_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineSpinner.setAdapter(adapter);
    }

    protected void sendMessage() {
        Intent URL = new Intent(android.content.Intent.ACTION_VIEW);
        URL.setData(Uri.parse("http://www.google.com/"));
        Log.d(TAG, "Accessed Google.");
        startActivity(URL);
    }

}
