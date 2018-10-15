package com.concrete_calculator.reinforcement;

public class ReinforcementProperties {

    Rebar bottomReinforcement;
    Rebar topReinforcement;
    double bottomCoverage;
    double topCoverage;

    //TODO: Add Stirrups to implementation
    public ReinforcementProperties(Rebar bottomReinforcement, Rebar topReinforcement, double bottomCoverage, double topCoverage) {
        this.bottomReinforcement = bottomReinforcement;
        this.topReinforcement = topReinforcement;
        this.bottomCoverage = bottomCoverage;
        this.topCoverage = topCoverage;
    }

    public Rebar getBottomReinforcement() {
        return bottomReinforcement;
    }

    public void setBottomReinforcement(Rebar bottomReinforcement) {
        this.bottomReinforcement = bottomReinforcement;
       }

    public Rebar getTopReinforcement() {
        return topReinforcement;
    }

    public void setTopReinforcement(Rebar topReinforcement) {
        this.topReinforcement = topReinforcement;
       }

    public double getBottomCoverage() {
        return bottomCoverage;
    }

    public void setBottomCoverage(double bottomCoverage) {
        this.bottomCoverage = bottomCoverage;
    }

    public double getTopCoverage() {
        return topCoverage;
    }

    public void setTopCoverage(double topCoverage) {
        this.topCoverage = topCoverage;
    }
}
