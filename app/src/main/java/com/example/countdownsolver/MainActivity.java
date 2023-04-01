package com.example.countdownsolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private NumberSolver numberSolver;
    private LetterSolver letterSolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup settings button
        ((FloatingActionButton)findViewById(R.id.floatingSettingsButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });

        //Initialise solver classes
        numberSolver = new NumberImplimentation();
        letterSolver = new LettersImplimentation();
    }


    public void solveLetters(View v)
    {

        EditText inputLetterBox = findViewById(R.id.inputLetterBox);
        String letters = inputLetterBox.getText().toString();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Solve(letters);
            }
        };
        Thread t = new Thread(runnable);
        t.start();

    }

    public void clearTextInput(View v)
    {
        ((EditText)v).setText("");
    }

    class NumberSolveThread implements Runnable
    {
        private Integer[] myNumberInputInts;
        private int myTarget;
        private Context mContext;
        public NumberSolveThread(Integer[] numberInputInts, int target, Context mContext)
        {
            myNumberInputInts = numberInputInts;
            myTarget = target;
            this.mContext = mContext;
        }

        @Override
        public void run() {
            String result = numberSolver.SolveNumbers(myNumberInputInts, myTarget);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == null)
                    {
                        Toast.makeText(mContext, "Unsolvable", Toast.LENGTH_LONG).show();
                        ((TextView)findViewById(R.id.stepResult)).setText("Steps will be displayed here.");
                        return;
                    }
                    ((TextView)findViewById(R.id.stepResult)).setText(result);
                }
            });
        }
    }


    public void solveNumbers(View v)
    {
        //Get all required inputs
        String numberInputs = ((EditText) findViewById(R.id.numbersInput)).getText().toString();
        String targetInput = ((EditText)findViewById(R.id.TargetInput)).getText().toString();
        try{
            List<String> numberInputList = Arrays.asList(numberInputs.split(", *"));
            Integer[] numberInputInts = numberInputList.stream().map(Integer::parseInt).collect(Collectors.toList()).toArray(new Integer[0]);
            int target = Integer.parseInt(targetInput);
            if (!InputValidator.validateNumbers(numberInputInts) || !InputValidator.validateTarget(target)){
                throw new NumberFormatException();
            }
            ((TextView)findViewById(R.id.stepResult)).setText("Solving...");

            //Create worker thread to solve
            NumberSolveThread nst = new NumberSolveThread(numberInputInts, target, this);
            Thread t = new Thread(nst);
            t.start();
            return;

        }
        catch(NumberFormatException err)
        {
            Toast.makeText(this, "Format of Numbers Invalid", Toast.LENGTH_LONG).show();

            ((TextView)findViewById(R.id.stepResult)).setText("Invalid number input\nMust enter 6 comma-separated numbers and a valid target.");
        }


    }




    public void SolveAnagrams(View v)
    {
        String letters = ((EditText)findViewById(R.id.inputLetterBox)).getText().toString();
        if (!InputValidator.validateLetterInput(letters))
        {
            Toast.makeText(getApplicationContext(), "Invalid letter input", Toast.LENGTH_LONG).show();

        }
        //Create Background worker
        Thread t = new Thread(){
            @Override
            public void run()
            {
                try{
                    String[] result = Solver.solveAnagram(letters, getApplicationContext());
                    if (result.length == 0)
                    {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "No solutions", Toast.LENGTH_LONG);
                        });
                        return;
                    }
                    runOnUiThread(() -> {
                        Intent i = new Intent(getApplicationContext(), AnagramActivity.class);
                        i.putExtra("MESSAGE", String.join("\n", result));
                        startActivity(i);
                    });
                }
                catch(IOException err)
                {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "File error", Toast.LENGTH_LONG);
                    });
                    return;
                }
            }
        };
        t.start();
    }


    public void Solve(String letters)
    {
        //Validate input
        if (!InputValidator.validateLetterInput(letters))
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Format of letters Invalid", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        //Get list of words
        ArrayList<String> words;
        try{
            words = FileHandler.getAllWords("words.txt", this);
        }
        catch (IOException e)
        {
            //TODO: Display file read error
            Log.d("DebugMessage", e.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView resultView = findViewById(R.id.LetterResultView);
                    Toast.makeText(getApplicationContext(), "File error", Toast.LENGTH_LONG).show();
                }
            });

            //Escape function if file error occurs.
            return;
        }


        //Solve
        HashMap<Character, Integer> letterDict = Solver.getDictionary(letters);

        String word = this.letterSolver.SolveLetters(words, letters);

        if (word == null)
        {
            //Display unsolvable error message
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView resultView = findViewById(R.id.LetterResultView);
                    Toast.makeText(getApplicationContext(), "No solutions", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        //Display word
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView letterResultView = findViewById(R.id.LetterResultView);
                letterResultView.setText("Solved: " + word);
            }
        });

    }



}