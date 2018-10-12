package com.concrete_calculator;

public class ReinforcementProperties {

    Rebar bottomReinforcement;
    Rebar topReinforcement;
    double bottomCoverage;
    double topCoverage;

    public ReinforcementProperties(Rebar bottomReinforcement, Rebar topReinforcement, double bottomCoverage, double topCoverage) {
        this.bottomReinforcement = bottomReinforcement;
        this.topReinforcement = topReinforcement;
        this.bottomCoverage = bottomCoverage;
        this.topCoverage = topCoverage;
    }
}
