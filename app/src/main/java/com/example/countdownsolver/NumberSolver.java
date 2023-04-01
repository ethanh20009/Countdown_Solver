package com.example.countdownsolver;

public interface NumberSolver {

    /**
     * For every step required, adds step to return string.
     * Returns empty string if number already exists in list.
     * returns None if not possible on route.
     * @param numbers
     * @param target
     * @return "" if found, null if impossible and string of steps if steps required
     */
    String SolveNumbers(Integer[] numbers, int target);
}
