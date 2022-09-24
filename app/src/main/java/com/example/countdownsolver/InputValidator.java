package com.example.countdownsolver;

public abstract class InputValidator {

    public static boolean validateLetterInput(String letters)
    {
        if (!(letters.replaceAll("[A-z]", "").equals(""))) //If not only A-z
        {
            return false;
        }
        if (Settings.isRestrictingToCowntdown)
        {
            if (letters.length() != 9) {return false;}
            return true;
        }
        else{
            if (letters.length() > 20 || letters.length() < 4) {return false;}
            return true;
        }
    }

    public static boolean validateNumbers(Integer[] numbers)
    {
        if (numbers.length > 7){return false;}
        if (numbers.length < 2){return false;}
        if (Settings.isRestrictingToCowntdown && numbers.length != 6)
        {
            return false;
        }
        return true;
    }

    public static boolean validateTarget(int target)
    {
        if (Settings.isRestrictingToCowntdown)
        {
            if (target < 100 || target > 999)
            {
                return false;
            }
        }
        return true;
    }




}
