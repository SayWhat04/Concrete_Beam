package com.concrete_calculator.reinforcement;

public class ReinforcementProperties {

    Rebar bottomReinforcement;
    Rebar topReinforcement;
    Rebar stirrup;
    double bottomCoverage;
    double topCoverage;

    double bottomReinforcementCrossSection;
    double topReinforcementCrossSection;
    double stirrupsCrossSection;

    int bottomReinforcementQuantity;
    int topReinforcementQuantity;
    int stirrupsNumberOfArms;

    double thetaAngleShear;

    public ReinforcementProperties(Rebar bottomReinforcement, Rebar topReinforcement, Rebar stirrup, double bottomCoverage, double topCoverage) {
        this.bottomReinforcement = bottomReinforcement;
        this.topReinforcement = topReinforcement;
        this.stirrup = stirrup;
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

    public Rebar getStirrup() {
        return stirrup;
    }

    public void setStirrup(Rebar stirrup) {
        this.stirrup = stirrup;
    }

    public double getBottomReinforcementCrossSection() {
        return bottomReinforcementCrossSection;
    }

    public void setBottomReinforcementCrossSection(double bottomReinforcementCrossSection) {
        this.bottomReinforcementCrossSection = bottomReinforcementCrossSection;
    }

    public double getTopReinforcementCrossSection() {
        return topReinforcementCrossSection;
    }

    public void setTopReinforcementCrossSection(double topReinforcementCrossSection) {
        this.topReinforcementCrossSection = topReinforcementCrossSection;
    }

    public double getThetaAngleShear() {
        return thetaAngleShear;
    }

    public void setThetaAngleShear(double thetaAngleShear) {
        this.thetaAngleShear = thetaAngleShear;
    }

    public double getStirrupsCrossSection() {
        return stirrupsCrossSection;
    }

    public void setStirrupsCrossSection(double stirrupsCrossSection) {
        this.stirrupsCrossSection = stirrupsCrossSection;
    }
}
