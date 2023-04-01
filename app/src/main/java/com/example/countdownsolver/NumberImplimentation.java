package com.example.countdownsolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberImplimentation implements NumberSolver {
    @Override
    public String SolveNumbers(Integer[] numbers, int target)
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
}
