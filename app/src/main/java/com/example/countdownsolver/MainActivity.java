package com.example.countdownsolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void setupSettings()
    {
        Settings.restrictingToCowntdown = ((Switch)findViewById(R.id.cdInputRestrictorSwitch)).isChecked();

    }

    public void solveLetters(View v)
    {

        EditText inputLetterBox = findViewById(R.id.inputLetterBox);
        String letters = inputLetterBox.getText().toString();
        Solve(letters);

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
            String result = SolveNumbers(myNumberInputInts, myTarget);
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
            if (numberInputInts.length != 6){
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

    /**
     * For every step required, adds step to return string.
     * Returns empty string if number already exists in list.
     * returns None if not possible on route.
     * @param numbers
     * @param target
     * @return "" if found, null if impossible and string of steps if steps required
     */
    private String SolveNumbers(Integer[] numbers, int target)
    {
        List<Integer> numbersList = Arrays.asList(numbers);
        for (int n : numbers)
        {
            if (n == target)
            {
                return ""; //Works, return Success
            }
        }
        if (numbers.length == 1){return null;}
        for (int i = 1; i < numbers.length; i++)
        {
            for (int j = 0; j < i; j++)
            {
                //Every possible combination not including itself
                int a = numbers[i];
                int b = numbers[j];
                String resultString;
                ArrayList<Integer> ammended = new ArrayList<Integer>(numbersList);
                ammended.remove((Object)a);
                ammended.remove((Object)b);

                String step;
                int newValue;
                List<Integer> preparedList;

                //Do every possible order of operation

                //Add
                step = "Add " + String.valueOf(a) + " and " + String.valueOf(b);
                newValue = a+b;
                preparedList = new ArrayList<>(ammended);
                preparedList.add(newValue);
                resultString = SolveNumbers(preparedList.toArray(new Integer[0]), target);
                if (resultString != null) //Success
                {
                    return step + ",\n" + resultString;
                }

                //Multiply
                step = "Multiply " + String.valueOf(a) + " and " + String.valueOf(b);
                newValue = a*b;
                preparedList = new ArrayList<>(ammended);
                preparedList.add(newValue);
                resultString = SolveNumbers(preparedList.toArray(new Integer[0]), target);
                if (resultString != null) //Success
                {
                    return step + ",\n" + resultString;
                }

                //Divide
                //Make sure for a/b -> a > b then check if divides evenly.
                if (a < b){ //Ensures a >= b
                    int temp = a;
                    a = b;
                    b = temp;
                }
                if (b != 0 && a%b == 0){
                    step = "Divide " + String.valueOf(a) + " by " + String.valueOf(b);
                    newValue = a/b;
                    preparedList = new ArrayList<>(ammended);
                    preparedList.add(newValue);
                    resultString = SolveNumbers(preparedList.toArray(new Integer[0]), target);
                    if (resultString != null) //Success
                    {
                        return step + ",\n" + resultString;
                    }
                }

                //Subtract
                //Make sure for a-b -> a > b
                if (a < b){ //Ensures a >= b
                    int temp = a;
                    a = b;
                    b = temp;
                }
                step = "Subtract " + String.valueOf(b) + " from " + String.valueOf(a);
                newValue = a-b;
                preparedList = new ArrayList<>(ammended);
                preparedList.add(newValue);
                resultString = SolveNumbers(preparedList.toArray(new Integer[0]), target);
                if (resultString != null) //Success
                {
                    return step + ",\n" + resultString;
                }

            }
        }
        return null;
    }

    public void Solve(String letters)
    {
        Log.d("State", letters);
        if (letters.length() != 9)
        {
            //TODO: Display that error has occurred due to incorrect format
            Log.d("DebugMessage", "Format of letters error");
            TextView resultView = findViewById(R.id.LetterResultView);
            Toast.makeText(this, "Format of letters Invalid", Toast.LENGTH_LONG).show();
            return;
        }

        //Solve
        HashMap<Character, Integer> letterDict = getDictionary(letters);

        try{

            ArrayList<String> words = FileHandler.getAllWords("words.txt", this);
            Log.d("DebugMessage", "Got Here");
            for (int i = words.size()-1; i > -1; i--)
            {
                String word = words.get(i);
                HashMap<Character, Integer> dictWord = getDictionary(word);
                boolean failed = false;
                for (char c : dictWord.keySet())
                {
                    if (!letterDict.containsKey(c))
                    {
                        failed = true;
                        break;
                    }
                    if (dictWord.get(c) > letterDict.get(c))
                    {
                        failed = true;
                        break;
                    }
                }
                if (!failed)
                {
                    //TODO: Answer stored in variable 'word'
                    TextView letterResultView = findViewById(R.id.LetterResultView);
                    letterResultView.setText("Solved: " + word);
                    Log.d("DebugMessage", "Solved: " + word);
                    return;
                }


            }
        }
        catch(IOException e)
        {
            //TODO: Display file read error
            Log.d("DebugMessage", e.toString());
            TextView resultView = findViewById(R.id.LetterResultView);
            Toast.makeText(this, "File error", Toast.LENGTH_LONG).show();

            return;
        }
    }

    private HashMap<Character, Integer> getDictionary(String string) {
        HashMap<Character, Integer> dict = new HashMap<>();
        for (char c : string.toCharArray()) {
            if (dict.containsKey(c)) {
                dict.put(c, dict.get(c) + 1);
            } else {
                dict.put(c, 1);
            }
        }
        return dict;
    }

}