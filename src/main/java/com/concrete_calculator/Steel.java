package com.concrete_calculator;

public enum Steel {
    B_500(500),
    B_600(600);

    public static final double STEEL_PARTIAL_FACTOR = 1.15;

    private double f_yk;

    Steel(double f_yk) {
        this.f_yk = f_yk;
    }

    public double getF_yk() {
        return f_yk;
    }
}
