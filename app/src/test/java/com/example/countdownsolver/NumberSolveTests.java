package com.example.countdownsolver;


import org.junit.Assert;
import org.junit.Test;

public class NumberSolveTests {
    @Test
    public void TestNumberSolve()
    {
        NumberSolver withoutDynamic = new NumberImplimentation();
        NumberSolver withDynamic = new NumberSolverDynamic();

        Integer[] arr = {1,2,3,4,5,6,7};
        int target = -1;

        long startTime, endTime, dynamicTime, withoutDTime;

        startTime = System.nanoTime();
        String resultD = withDynamic.SolveNumbers(arr,target);
        endTime = System.nanoTime();
        dynamicTime = endTime-startTime;

        startTime = System.nanoTime();
        String result = withoutDynamic.SolveNumbers(arr,target);
        endTime = System.nanoTime();
        withoutDTime = endTime-startTime;

        System.out.println(result);
        System.out.println("Time Without Dynamic");
        System.out.println(withoutDTime);
        System.out.println("Time With Dynamic");
        System.out.println(dynamicTime);
    }
}
