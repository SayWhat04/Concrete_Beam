package com.concrete_calculator.utils;

public class CalculationUtils {

    public static int roundUpToNearestSelectedValue(double numberToRound, int accuracy) {
        return (int) (Math.ceil(numberToRound / accuracy) * accuracy);
    }

    public static double[] swapValuesInArray(double[] arrayToSwap) {
        double[] swappedArray = {arrayToSwap[1], arrayToSwap[0]};
        return swappedArray;
    }

    public static double calculateCrossSectionAreaOfBars(int barDiameter, int numberOfBars) {
        return (Math.PI * Math.pow(barDiameter, 2) * numberOfBars) / (4);
    }
}
