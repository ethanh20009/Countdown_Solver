package com.example.countdownsolver;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LettersImplimentation implements LetterSolver{
    @Override
    public String SolveLetters(ArrayList<String> words, String letters) {
        HashMap<Character, Integer> letterDict = Solver.getDictionary(letters);

        for (int i = words.size()-1; i > -1; i--) {
            String word = words.get(i);
            HashMap<Character, Integer> dictWord = Solver.getDictionary(word);
            boolean failed = false;
            for (char c : dictWord.keySet()) {
                if (!letterDict.containsKey(c)) {
                    failed = true;
                    break;
                }
                if (dictWord.get(c) > letterDict.get(c)) {
                    failed = true;
                    break;
                }
            }
            if (!failed) {
                return word;
            }
        }
        return null;
    }
}
