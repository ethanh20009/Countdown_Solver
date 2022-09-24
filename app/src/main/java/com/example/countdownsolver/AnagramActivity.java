package com.example.countdownsolver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AnagramActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagram);
        ((Button)findViewById(R.id.anagramDoneButton)).setOnClickListener((view) -> {
            finish();
        });
        String message;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                message= null;
            } else {
                message= extras.getString("MESSAGE");
            }
        } else {
            message= (String) savedInstanceState.getSerializable("MESSAGE");
        }
        ((TextView)findViewById(R.id.solvedAnagramsView)).setText(message);
    }
}