package com.example.countdownsolver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupSettings();

        ((Button)findViewById(R.id.doneButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupSettings()
    {
        Switch restrictorSwitch = (Switch)findViewById(R.id.cdInputRestrictorSwitch);
        restrictorSwitch.setChecked(Settings.isRestrictingToCowntdown);
        restrictorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.isRestrictingToCowntdown = b;
            }
        });


    }







}