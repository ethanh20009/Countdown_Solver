package com.example.countdownsolver;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileHandler {
    public static ArrayList<String> getAllWords(String filepath, Context mContext) throws IOException
    {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(mContext.getAssets().open(filepath))))
        {
            ArrayList<String> words = new ArrayList<>();
            String word;
            while ((word = br.readLine()) != null)
            {
                words.add(word);
            }
            //String[] wordStr = (String[])words.toArray();
            //Sort words

            words.sort((a,b) -> a.length() - b.length());

            return words;
        }
        catch(IOException e)
        {
            throw e;
        }
    }
}
