package com.example.mp5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.mp5.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent URL = new Intent(android.content.Intent.ACTION_VIEW);
        URL.setData(Uri.parse("http://www.google.com/"));
        /*
        EditText userInput = (EditText) findViewById(R.id.inputBox);
        String message = userInput.getText().toString();
        URL.putExtra(EXTRA_MESSAGE, message);
        */
        startActivity(URL);
    }
}
