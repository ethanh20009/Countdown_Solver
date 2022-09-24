package com.example.countdownsolver;

import android.content.Context;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Solver {
    public static String[] solveAnagram(String letters, Context mContext) throws IOException
    {
        String[] allWords;
        List<String> solvedAnagrams = new ArrayList<>();
        try{
            allWords = FileHandler.getAllWords("words.txt", mContext).stream()
                    .filter((s -> s.length() == letters.length()))
                    .collect(Collectors.toList()).toArray(new String[0]);

        }
        catch(IOException err){
            throw err;
        }
        HashMap<Character, Integer> lettersMap = getDictionary(letters);
        for (String word : allWords)
        {
            boolean working = true;
            HashMap<Character, Integer> wordMap = getDictionary(word);
            for (Character c : wordMap.keySet())
            {
                if (!lettersMap.containsKey(c))
                {
                    working = false;
                    break;
                }
                if (wordMap.get(c) != lettersMap.get(c))
                {
                    working = false;
                    break;
                }
            }
            if (working)
            {
                solvedAnagrams.add(word);
            }
        }
        return solvedAnagrams.toArray(new String[0]);
    }

    public static HashMap<Character, Integer> getDictionary(String string) {
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
