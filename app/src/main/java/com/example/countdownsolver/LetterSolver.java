package com.example.countdownsolver;

import java.util.ArrayList;

public interface LetterSolver {
     /**
      *
      * @param words A list of all words (normally retrieved from a text file)
      * @param letters A String containing all the letters to use
      * @return A string containing the best word. Null if unsolved
      */
     String SolveLetters(ArrayList<String> words, String letters);


}
